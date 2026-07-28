/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  okhttp3.HttpUrl
 *  okhttp3.Interceptor
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  okhttp3.Request
 *  org.bouncycastle.asn1.ASN1ObjectIdentifier
 *  org.bouncycastle.asn1.ASN1Primitive
 *  org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
 *  org.bouncycastle.asn1.pkcs.PrivateKeyInfo
 *  org.bouncycastle.asn1.x9.X9ObjectIdentifiers
 *  org.bouncycastle.cert.X509CertificateHolder
 *  org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.openssl.PEMKeyPair
 *  org.bouncycastle.openssl.PEMParser
 *  retrofit2.Converter$Factory
 *  retrofit2.Retrofit
 *  retrofit2.Retrofit$Builder
 *  retrofit2.converter.jackson.JacksonConverterFactory
 */
package dev.langchain4j.store.embedding.vespa;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.vespa.VespaApi;
import dev.langchain4j.store.embedding.vespa.VespaRequestLoggingInterceptor;
import dev.langchain4j.store.embedding.vespa.VespaResponseLoggingInterceptor;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class VespaClient {
    static final BouncyCastleProvider bcProvider = new BouncyCastleProvider();

    VespaClient() {
    }

    public static VespaApi createInstance(String baseUrl, Path certificate, Path privateKey, boolean logRequests, boolean logResponses) {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request request = chain.request();
                if (request.url().url().getPath().startsWith("/search/")) {
                    HttpUrl url = request.url().newBuilder().removePathSegment(1).addPathSegment("").encodedQuery((String)request.url().encodedPathSegments().get(1)).build();
                    request = request.newBuilder().url(url).build();
                }
                return chain.proceed(request);
            });
            VespaClient.addSsl(certificate, privateKey, builder);
            if (logRequests) {
                builder.addInterceptor((Interceptor)new VespaRequestLoggingInterceptor());
            }
            if (logResponses) {
                builder.addInterceptor((Interceptor)new VespaResponseLoggingInterceptor());
            }
            OkHttpClient client = builder.build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Utils.ensureTrailingForwardSlash((String)baseUrl)).client(client).addConverterFactory((Converter.Factory)JacksonConverterFactory.create()).build();
            return (VespaApi)retrofit.create(VespaApi.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addSsl(Path certificate, Path privateKey, OkHttpClient.Builder builder) throws IOException, GeneralSecurityException {
        if (certificate != null && privateKey != null) {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(null);
            keystore.setKeyEntry("cert", VespaClient.privateKey(privateKey), new char[0], VespaClient.certificates(certificate));
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(VespaClient.createKeyManagers(keystore), null, null);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager)trustManagerFactory.getTrustManagers()[0]);
        }
    }

    private static KeyManager[] createKeyManagers(KeyStore keystore) throws GeneralSecurityException {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keystore, new char[0]);
        return kmf.getKeyManagers();
    }

    private static Certificate[] certificates(Path file) throws IOException, GeneralSecurityException {
        try (PEMParser parser = new PEMParser((Reader)Files.newBufferedReader(file));){
            Object pemObject;
            ArrayList<X509Certificate> result = new ArrayList<X509Certificate>();
            while ((pemObject = parser.readObject()) != null) {
                result.add(VespaClient.toX509Certificate(pemObject));
            }
            if (result.isEmpty()) {
                throw new IOException("File contains no PEM encoded certificates: " + file);
            }
            Certificate[] certificateArray = result.toArray(new Certificate[0]);
            return certificateArray;
        }
    }

    private static PrivateKey privateKey(Path file) throws IOException, GeneralSecurityException {
        try (PEMParser parser = new PEMParser((Reader)Files.newBufferedReader(file));){
            Object pemObject;
            while ((pemObject = parser.readObject()) != null) {
                if (pemObject instanceof PrivateKeyInfo) {
                    PrivateKeyInfo keyInfo = (PrivateKeyInfo)pemObject;
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyInfo.getEncoded());
                    PrivateKey privateKey = VespaClient.createKeyFactory(keyInfo).generatePrivate(keySpec);
                    return privateKey;
                }
                if (!(pemObject instanceof PEMKeyPair)) continue;
                PEMKeyPair pemKeypair = (PEMKeyPair)pemObject;
                PrivateKeyInfo keyInfo = pemKeypair.getPrivateKeyInfo();
                PrivateKey privateKey = VespaClient.createKeyFactory(keyInfo).generatePrivate(new PKCS8EncodedKeySpec(keyInfo.getEncoded()));
                return privateKey;
            }
            throw new IOException("Could not find private key in PEM file");
        }
    }

    private static X509Certificate toX509Certificate(Object pemObject) throws IOException, GeneralSecurityException {
        if (pemObject instanceof X509Certificate) {
            return (X509Certificate)pemObject;
        }
        if (pemObject instanceof X509CertificateHolder) {
            return new JcaX509CertificateConverter().setProvider((Provider)bcProvider).getCertificate((X509CertificateHolder)pemObject);
        }
        throw new IOException("Invalid type of PEM object: " + pemObject);
    }

    private static KeyFactory createKeyFactory(PrivateKeyInfo info) throws IOException, GeneralSecurityException {
        ASN1ObjectIdentifier algorithm = info.getPrivateKeyAlgorithm().getAlgorithm();
        if (X9ObjectIdentifiers.id_ecPublicKey.equals((ASN1Primitive)algorithm)) {
            return KeyFactory.getInstance("EC", (Provider)bcProvider);
        }
        if (PKCSObjectIdentifiers.rsaEncryption.equals((ASN1Primitive)algorithm)) {
            return KeyFactory.getInstance("RSA", (Provider)bcProvider);
        }
        throw new IOException("Unknown key algorithm: " + algorithm);
    }
}


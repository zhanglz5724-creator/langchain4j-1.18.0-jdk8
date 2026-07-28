/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.micrometer.common.docs.KeyName
 *  io.micrometer.observation.Observation$Context
 *  io.micrometer.observation.ObservationConvention
 *  io.micrometer.observation.docs.ObservationDocumentation
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.observation.convention;

import dev.langchain4j.observation.convention.DefaultChatModelConvention;
import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.docs.ObservationDocumentation;
import org.jspecify.annotations.Nullable;

public enum ChatModelDocumentation implements ObservationDocumentation
{
    INSTANCE{

        public @Nullable Class<? extends ObservationConvention<? extends // Could not load outer class - annotation placement on inner may be incorrect
        Observation.Context>> getDefaultConvention() {
            return DefaultChatModelConvention.class;
        }

        public KeyName[] getLowCardinalityKeyNames() {
            return LowCardinalityValues.values();
        }

        public KeyName[] getHighCardinalityKeyNames() {
            return HighCardinalityValues.values();
        }
    };


    public static enum HighCardinalityValues implements KeyName
    {
        OUTPUT_TOKENS{

            public String asString() {
                return "output_tokens";
            }
        }
        ,
        INPUT_TOKENS{

            public String asString() {
                return "input_tokens";
            }
        };

    }

    public static enum LowCardinalityValues implements KeyName
    {
        OPERATION_NAME{

            public String asString() {
                return "gen_ai.operation.name";
            }
        }
        ,
        PROVIDER_NAME{

            public String asString() {
                return "gen_ai.provider.name";
            }
        }
        ,
        SYSTEM{

            public String asString() {
                return "gen_ai.system";
            }
        }
        ,
        REQUEST_MODEL{

            public String asString() {
                return "gen_ai.request.model";
            }
        }
        ,
        RESPONSE_MODEL{

            public String asString() {
                return "gen_ai.response.model";
            }
        }
        ,
        TOKEN_TYPE{

            public String asString() {
                return "gen_ai.token.type";
            }
        }
        ,
        OUTCOME{

            public String asString() {
                return "outcome";
            }
        };

    }
}


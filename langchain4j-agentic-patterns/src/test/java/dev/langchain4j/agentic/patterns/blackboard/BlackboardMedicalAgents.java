package dev.langchain4j.agentic.patterns.blackboard;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.observability.MonitoredAgent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class BlackboardMedicalAgents {

    public interface SymptomExtractor {

        @UserMessage("                You are a medical symptom extraction specialist.\n"
+ "                Extract and summarize the key symptoms from the following patient description.\n"
+ "                List each symptom clearly and concisely.\n"
+ "                Patient description: {{patientInput}}\n"
+ "                ")
        @Agent(value = "Extract symptoms from patient input", outputKey = "symptoms")
        String extractSymptoms(@V("patientInput") String patientInput);
    }

    public interface LabResultAnalyzer {

        @UserMessage("                You are a clinical laboratory specialist.\n"
+ "                Analyze the following lab results and provide a summary of findings,\n"
+ "                highlighting any abnormal values and their clinical significance.\n"
+ "                Lab results: {{labResults}}\n"
+ "                ")
        @Agent(value = "Analyze lab results", outputKey = "labAnalysis")
        String analyzeLabResults(@V("labResults") String labResults);
    }

    public interface DrugInteractionChecker {

        @UserMessage("                You are a pharmacology specialist.\n"
+ "                Based on the patient's symptoms and current medications,\n"
+ "                identify any potential drug interactions or contraindications.\n"
+ "                Symptoms: {{symptoms}}\n"
+ "                Current medications: {{medications}}\n"
+ "                ")
        @Agent(value = "Check drug interactions based on symptoms and medications", outputKey = "drugInteractions")
        String checkInteractions(@V("symptoms") String symptoms, @V("medications") String medications);
    }

    public interface DiagnosisAgent {

        @UserMessage("                You are an experienced diagnostician.\n"
+ "                Based on the extracted symptoms and lab analysis, provide a preliminary diagnosis.\n"
+ "                Consider the most likely conditions and any differential diagnoses.\n"
+ "                Symptoms: {{symptoms}}\n"
+ "                Lab analysis: {{labAnalysis}}\n"
+ "                ")
        @Agent(value = "Provide diagnosis based on symptoms and lab analysis", outputKey = "diagnosis")
        String diagnose(@V("symptoms") String symptoms, @V("labAnalysis") String labAnalysis);
    }

    public interface MedicalDiagnostics extends MonitoredAgent {

        @Agent
        String diagnose(@V("patientInput") String patientInput,
                        @V("labResults") String labResults,
                        @V("medications") String medications);
    }
}

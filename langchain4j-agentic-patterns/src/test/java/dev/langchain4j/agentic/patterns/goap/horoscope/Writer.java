package dev.langchain4j.agentic.patterns.goap.horoscope;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface Writer {
    @UserMessage("                Create an amusing writeup for {{person}} based on the following:\n"
+ "                - their horoscope: {{horoscope}}\n"
+ "                - a current news story: {{story}}\n"
+ "                ")
    @Agent("Create an amusing writeup for the target person based on their horoscope and current news stories")
    String write(@V("person") Person person, @V("horoscope") String horoscope, @V("story") String story);
}

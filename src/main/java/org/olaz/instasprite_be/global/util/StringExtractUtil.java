package org.olaz.instasprite_be.global.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Utility class for extracting mentions and hashtags from text
 * Used for parsing post captions and comments
 */
@Component
public class StringExtractUtil {

    /**
     * Extracts mentions (@username) from input text, excluding specified usernames
     * @param input The text to extract mentions from
     * @param exceptUsernames List of usernames to exclude from the result
     * @return List of mentioned usernames (without @ symbol)
     */
    public List<String> extractMentionsWithExceptList(String input, List<String> exceptUsernames) {
        final Set<String> mentions = new HashSet<>();
        final String regex = "@[0-9a-zA-Z_]+";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            mentions.add(matcher.group().substring(1));
        }
        
        for (String username : exceptUsernames) {
            mentions.remove(username);
        }

        return new ArrayList<>(mentions);
    }

    /**
     * Extracts all mentions (@username) from input text
     * @param input The text to extract mentions from
     * @return List of mentioned usernames (without @ symbol)
     */
    public List<String> extractMentions(String input) {
        return extractMentionsWithExceptList(input, List.of());
    }

    /**
     * Extracts all hashtags (#tag) from input text
     * @param input The text to extract hashtags from
     * @return List of hashtags (without # symbol)
     */
    public List<String> extractHashtags(String input) {
        final Set<String> hashtags = new HashSet<>();
        final String regex = "#[0-9a-zA-Z_]+";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            hashtags.add(matcher.group().substring(1));
        }

        return new ArrayList<>(hashtags);
    }
}


package dev.spaxter.curseguard.util;

import dev.spaxter.curseguard.logging.Logger;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

public class AsyncExecutor {

    public AsyncExecutor() {

    }

    public static ArrayList<String> findWordsAsync(final String input, final Set<String> wordList) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<CompletableFuture<Optional<String>>> futures = new ArrayList<>();

        for (String word : wordList) {
            CompletableFuture<Optional<String>> future = CompletableFuture.supplyAsync(() -> {
                if (input.contains(word)) {
                    return Optional.of(word);
                }
                return Optional.empty();
            });
            futures.add(future);
        }

        long start = System.currentTimeMillis();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        for (CompletableFuture<Optional<String>> future : futures) {
            future.thenAccept(result -> result.ifPresent(output::add));
        }

        try {
            allFutures.get();
        } catch (InterruptedException | ExecutionException e) {
            Logger.error("Word search future was interrupted or failed to execute: " + e.getMessage());
        }

        long stop = System.currentTimeMillis();

        Logger.debug("Searched through " + Ansi.GREEN + wordList.size() + Ansi.CYAN + " words in " + Ansi.GREEN + (stop - start) + "ms");
        Logger.debug("Word search found the following words: " + Ansi.YELLOW + String.join(", ", output));

        return output;
    }

}

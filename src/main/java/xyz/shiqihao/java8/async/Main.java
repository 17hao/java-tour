package xyz.shiqihao.java8.async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Main m = new Main();
        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.print("Sequentially: ");
        m.printExecTime(Main::findPrices, "iPhone11");
        System.out.print("Parallel: ");
        m.printExecTime(Main::findPricesParallel, "iPhone11");
        System.out.print("CompletableFuture: ");
        m.printExecTime(Main::findPricesFuture, "iPhone11");
    }

    /**
     * Finding prices sequentially.
     */
    static List<String> findPrices(String product) {
        return Shop.valueOf(13).stream()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    /**
     * Finding prices parallel.
     */
    static List<String> findPricesParallel(String product) {
        return Shop.valueOf(13).parallelStream()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    /**
     * Finding prices in future.
     */
    static List<String> findPricesFuture(String product) {
        List<CompletableFuture<String>> priceFuture = Shop.valueOf(13).stream()
                .map(shop ->
                        CompletableFuture.supplyAsync(() ->
                                String.format("%s price is %.2f",
                                        shop.getName(), shop.getPrice(product)), executor))
                .collect(Collectors.toList());

        return priceFuture.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    // static CompletableFuture<List<String>> findPriceFutureV2(String product) {
    //     return CompletableFuture.supplyAsync(() -> Shop.valueOf(12).stream()
    //             .map(shop -> String.format("%s price is %.2f",
    //                     shop.getName(), shop.getPrice(product)))
    //             .collect(Collectors.toList()));
    // }

    private void printExecTime(PriceFinder<String> p, String product) {
        long start = System.nanoTime();
        p.findPrice(product);
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " ms");
    }

    private static Executor executor = Executors.newFixedThreadPool(
            Math.min(Shop.valueOf(13).size(), 100), r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });
}

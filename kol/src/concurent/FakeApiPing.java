package concurent;

import java.util.*;
import java.util.concurrent.*;

public class FakeApiPing {

    // Result holder
    public static class ApiResult {
        public final int requestId;
        public final boolean success;
        public final String value;

        public ApiResult(int requestId, boolean success, String value) {
            this.requestId = requestId;
            this.success = success;
            this.value = value;
        }

        @Override
        public String toString() {
            return "ApiResult{" +
                    "requestId=" + requestId +
                    ", success=" + success +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class Api {
        public static ApiResult get(int requestId, int parameter) throws InterruptedException {
            long delayMillis = parameter * 100L;
            Thread.sleep(delayMillis);

            String response = "VALUE_" + parameter;
            return new ApiResult(requestId, true, response);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(); // number of API calls

        List<Callable<ApiResult>> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int parameter = sc.nextInt();

            // requestId is the loop index
            int requestId = i+1;
            //TODO add a Callable that invokes the API get method in the tasks list
            tasks.add(() -> Api.get(requestId,parameter));
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<ApiResult>> futures = new ArrayList<>();
        //TODO submit all callables to the executure and get the Futures
        for(Callable<ApiResult> t : tasks){
            futures.add(executor.submit(t));
        }

        List<ApiResult> results = new ArrayList<>();

        long timeoutMillis = 200;

        //TODO get the ApiResult from all the futures and allow a max timeout of timeoutMillis
        for(int i=0;i<futures.size();i++){
            Future<ApiResult> f = futures.get(i);

            try {
                results.add(f.get(timeoutMillis,TimeUnit.MILLISECONDS));
            }catch (TimeoutException e){
                results.add(new ApiResult(i+1,false,"TIMEOUT"));
            }catch (ExecutionException e){
                results.add(new ApiResult(i+1,false,"ERROR"));
            }
        }

        executor.shutdown();

        // Sorting by requestId
        results.sort(Comparator.comparingInt(r -> r.requestId));

        // Output
        for (ApiResult r : results) {
            System.out.printf(
                    "%d %s %s%n",
                    r.requestId,
                    r.success ? "OK" : "FAILED",
                    r.value
            );
        }
    }
}

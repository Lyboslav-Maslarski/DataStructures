package solutions;

import java.util.PriorityQueue;
import java.util.Queue;

public class CookiesProblem {
    public Integer solve(int requiredSweetness, int[] cookiesSweetness) {
        Queue<Integer> cookies = new PriorityQueue<>();
        for (int cookie : cookiesSweetness) {
            cookies.offer(cookie);
        }
        int currentMinSweetness = cookies.peek();
        int steps = 0;
        while (currentMinSweetness < requiredSweetness && cookies.size() > 1) {
            steps++;
            Integer leastSweetCookie = cookies.poll();
            Integer secondLeastSweetCookie = cookies.poll();
            int combined = leastSweetCookie + 2 * secondLeastSweetCookie;
            cookies.add(combined);
            currentMinSweetness = cookies.peek();
        }
        return currentMinSweetness > requiredSweetness ? steps : -1;
    }
}

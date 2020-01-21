import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import org.junit.Test;

public class FeedforwardTest {
    @Test
    public void testFeedforward() {
        SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(.343, .0462, .00316);
        System.out.println(feedforward.calculate(6370.0 / 600));
        System.out.println(-feedforward.calculate(6370.0  / 600));
    }
}

package clickfiletoexecutor

import org.change.parser.clickfile.ClickToAbstractNetwork
import org.change.v2.analysis.expression.concrete.ConstantValue
import org.change.v2.executor.clickabstractnetwork._
import org.scalatest.{Matchers, FlatSpec}

/**
 * Author: Radu Stoenescu
 * Don't be a stranger,  symnetic.7.radustoe@spamgourmet.com
 */
class ClickToExecutorTests extends FlatSpec with Matchers {

  "A src-dst click" should "generate a valid no op executor" in {
    val absNet = ClickToAbstractNetwork.buildConfig("src/main/resources/click_test_files/SrcDst.click")
    val executor = ClickExecutionContextBuilder.buildExecutionContext(absNet)

    executor shouldBe a [ClickExecutionContext]
  }

  "A src-dst click executor" should "propagate the bing-bang state to dst, becoming stuck" in {
    val absNet = ClickToAbstractNetwork.buildConfig("src/main/resources/click_test_files/SrcDst.click")
    val executor = ClickExecutionContextBuilder.buildExecutionContext(absNet)

    var crtExecutor = executor
    while(! crtExecutor.isDone) {
      crtExecutor = crtExecutor.execute
    }

    crtExecutor.stuckStates.length should be (1)
    crtExecutor.stuckStates.head.history.length should be (4)
  }

  "A src-paint-dst executor" should "correctly paint the bloody flow" in {
    val absNet = ClickToAbstractNetwork.buildConfig("src/main/resources/click_test_files/SrcPaintDst.click")
    val executor = ClickExecutionContextBuilder.buildExecutionContext(absNet)

    var crtExecutor = executor
    while(! crtExecutor.isDone) {
      crtExecutor = crtExecutor.execute
    }

    crtExecutor.stuckStates.length should be (1)
    crtExecutor.stuckStates.head.history.length should be (6)
    crtExecutor.stuckStates.head.memory.eval("COLOR").get.e should be (ConstantValue(10))
  }

}

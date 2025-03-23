import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

abstract class NuXmvCodeInsightFixtureTestCase : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return File("src/test/testData/$basePath").absolutePath
    }
}

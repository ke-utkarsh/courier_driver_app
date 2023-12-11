package `in`.ymsli.ymlibrary.utils
import android.os.Environment
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class FileOperationsTest{

    @Test
    fun createFile() {

        FileOperations.createFile(File(Environment.getExternalStorageDirectory(),"Notes"))

    }


    @Test
    fun del() {

       assertFalse( FileOperations.deleteFile(""+Environment.getExternalStorageDirectory()+"Notes"))

    }

}
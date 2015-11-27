/**
 * Created with love by corbett.
 * User: corbett
 * Date: 12/2/14
 * Time: 10:46 AM
 */
package github.cworks.monitor

def baseUrl = new URL('https://absolutear.nhin.com/absolutear/loginAction.action');
def connection = baseUrl.openConnection();
connection.with({
    doOutput = true;
    requestMethod = 'POST';
    outputStream.withWriter() { writer ->
       writer << 'username=armon&password=iEATdirt123!'
    }

    println content.text;
});



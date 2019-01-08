import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.ibkglobal.common.util.StringUtils;

public class TestCase {
	public static void main(String args[]) throws IOException {
		String value = "1234567890위성규";
		byte[] encord_data = value.getBytes(Charset.forName("MS949"));

		String answer1 = String.format("%1$-" + 20 + "s", value);
		System.out.println("[" + answer1 + "]" + answer1.getBytes().length);
		String answer2 = String.format("%1$-" + 20 + "s", new String(encord_data, "MS949"));
		System.out.println("[" + answer2 + "]" + answer2.getBytes().length);
		String answer3 = String.format("%1$-" + 20 + "s", new String(value.getBytes(), "MS949"));
		System.out.println("[" + answer3 + "]" + answer3.getBytes().length);
		System.out.println(value.getBytes().length);
		System.out.println(value.getBytes("MS949").length);
		System.out.println(new String("위성규".getBytes(), "UTF-8"));
		System.out.println(System.getProperty("file.encoding"));

		ByteArrayInputStream byteArrayInputStream949 = new ByteArrayInputStream(value.getBytes("MS949"));
		ByteArrayInputStream byteArrayInputStreamUTF = new ByteArrayInputStream(value.getBytes());
		BufferedReader bf = new BufferedReader(new InputStreamReader(byteArrayInputStream949));
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = byteArrayInputStreamUTF.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();

		byte[] byteArray = buffer.toByteArray();

		System.out.println(new String(byteArray) + ":" + new String(byteArray).getBytes().length);
		System.out.println(new String(byteArray, "MS949") + ":" + new String(byteArray, "MS949").getBytes().length);
		StringWriter writer = new StringWriter();

		String encoding = "MS949";
		IOUtils.copy(byteArrayInputStream949, writer);

		Charset euckrCharset = Charset.forName("MS949");
		ByteBuffer byteBuffer = euckrCharset.encode(value);
		byte[] euckrStringBuffer = new byte[byteBuffer.remaining()];
		byteBuffer.get(euckrStringBuffer);

		// String helloString = "안녕하세요";
		// byte[] euckrStringBuffer = helloString.getBytes(Charset.forName("euc-kr"));
		//
		// euc-kr 로 변환된 byte 문자열을 다시 유니코드 String 으로 변환.
		// String 생성자의
		// 첫 번째 인자로 문자열 byte 배열을 넣어주고,
		// 두 번째 인자로 byte 배열의 인코딩 값을 넣어준다.
		String decodedHelloString = new String(euckrStringBuffer);
		System.out.println(decodedHelloString + ":" + decodedHelloString.getBytes().length);

		String helloString = "안녕하세요";
		System.out.println("Source : " + helloString);

		// String 을 euc-kr 로 인코딩.
		euckrStringBuffer = helloString.getBytes(Charset.forName("MS949"));
		System.out.println();

		System.out.println("euc-kr - length : " + euckrStringBuffer.length);
		String decodedFromEucKr = new String(euckrStringBuffer, "MS949");
		System.out.println("String from euc-kr : " + decodedFromEucKr.getBytes("MS949").length);

		String string = "한글";
		byte[] bytes = string.getBytes();
		for (byte b : bytes) {
			System.out.print(String.format("0x%02X ", b));
		}
		System.out.println();
		System.out.println("[" + String.format("%2$-20s%1$10s", value, "123") + "]");

		System.out.println(StringUtils.leftPading("한글", 10, ""));

		System.out.println(StringUtils.rightPading(value, 20, " ", Charset.forName("MS949")));

	}

}

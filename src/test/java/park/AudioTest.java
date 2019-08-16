package park;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;

public class AudioTest {
	public static void main(String[] args) {
		AudioTest at = new AudioTest();
		// 选择播放文件
		String fileName = at.getClass().getClassLoader().getResource("audio/占用车位成功.wav").getPath();
		// 选择播放文件
		File file = new File(fileName);
		// 创建audioclip对象
		AudioClip audioClip = null;
		try {
			// 将file转换为url
			audioClip = Applet.newAudioClip(file.toURL());
			// 循环播放 播放一次可以使用audioClip.play
			audioClip.play();
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

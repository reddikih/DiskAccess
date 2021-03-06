import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class DiskAccess {
	
	private static int disk = 5;	// 接続ディスク数
	private static int filesize = 1024;	// 読み書きするファイルサイズ
	private static int rationread = 7;	// read比率(割)
	private static int interval = 1000;	// 読み書きする間隔(ms)
	private static String mountpoint = "/newdisk";	// マウントポイント。末尾に連番でディスク番号が付く
	private static String rwfilename = "/dummy"; 	// 読み書きするファイル名
	
	private ArrayList<String> path = new ArrayList<String>();	// 読み書きするファイルパスを格納
	
	
	// ファイルをバイナリで読み込む
	private void read(String path) {
		File f = new File(path);
		try {
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			
			byte[] buf = new byte[filesize];
			
			bis.read(buf);
			System.out.println("read: " + f.getPath());
			
			bis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	// ファイルをバイナリで書き込む
	private void write(String path) {
		File f = new File(path);
		try {
			FileOutputStream fos = new FileOutputStream(f);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			byte[] buf = new byte[filesize];
			
			// 単純に0x01で埋める
			for(int i = 0; i < buf.length; i++) {
				buf[i] = 0x01;
			}
			
			bos.write(buf);
			bos.flush();
			System.out.println("write: " + f.getPath());
			
			bos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	// 読み書きするファイルパスを設定
	public void setPath(){
		for(int i = 1; i <= disk; i++) {
			path.add(mountpoint + i + rwfilename);
		}
	}
	
	
	// ランダムにアクセス
	public void randomAccess() {
		Random rnd = new Random();
		
		while(true) {
			if(rnd.nextInt(10) < rationread) {
				read(path.get(rnd.nextInt(disk)));
			}else {
				write(path.get(rnd.nextInt(disk)));
			}
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	
	public static void main(String[] args) {
		DiskAccess da = new DiskAccess();
		da.setPath();
		da.randomAccess();
	}
	
}

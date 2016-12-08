package virnet.experiment.assistantapi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class ExperimentSave {
	
	private static final String operationServerIP = "202.112.113.135";
	private static final int operationServerPort = 8342;
	private static final String FILEPATH = "D:/VirnetFileForSave";
	private String cabinet_NUM;		//ʵ������
	//private int timeout = 5000;
	private String filepath_info="";
	private Socket connectToServer;
	private DataOutputStream osToServer = null;	//������������������
	private DataInputStream isFromServer = null;	//�ӷ����������������
	private InputStream is = null;
	private String result = null;
    private String detail = null;
    
    public ExperimentSave(String cabinet_num) {
    	this.cabinet_NUM = cabinet_num;
    }
    public boolean save() {
    	try {
			connectToServer = new Socket(operationServerIP, operationServerPort);
			//connectToServer.setSoTimeout(timeout);
			// Create an input stream to receive data from the server
			is = connectToServer.getInputStream();
			isFromServer = new DataInputStream(is);
		    // Create an output stream to send data to the server
			osToServer = new DataOutputStream(connectToServer.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		JSONObject outputdata = new JSONObject();
		try {
			outputdata.put("command_name", "save");
			outputdata.put("cabinet_num", cabinet_NUM);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		String outputdata_str = outputdata.toString();
		try {
			osToServer.write(outputdata_str.getBytes(), 0, outputdata_str.getBytes().length);
			osToServer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		//ByteArrayOutputStream returnFromServer = null;
		//returnFromServer = new ByteArrayOutputStream();
		int count = 0;
		while (count == 0) {
    		try {
				count = isFromServer.available();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
    	}
		byte[] buffer=new byte[count];
    	int readCount = 0; // �Ѿ��ɹ���ȡ���ֽڵĸ���
    	while (readCount < count) {
    		try {
				readCount += isFromServer.read(buffer, readCount, count - readCount);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
    	}
        JSONObject returnjson = null;
        try {
			returnjson = new JSONObject(new String(buffer));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        try {
			result = returnjson.getString("result");
			detail = returnjson.getString("detail");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        if(result.equals("fail")) {
        	return false;
        }
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String filenum_str = null;	//�����ļ�����
		int filenum;
		try {
			filenum_str = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		File f = new File(FILEPATH);  
        if(!f.exists()){  
            f.mkdir();    
        }
		filenum = Integer.parseInt(filenum_str);
		String[] filepath_arr = new String[filenum];
		String thisfilepath = FILEPATH+"/";
		for(int i = 0;i < filenum;i++) {
			try {
				String thisfilename = br.readLine();
				System.out.println(thisfilename);
				//long len = isFromServer.readLong();
				String filelen = br.readLine();
				int len = Integer.parseInt(filelen);
				thisfilepath += thisfilename;
				filepath_arr[i] = thisfilepath;
				FileOutputStream fos = new FileOutputStream(new File(thisfilepath));      
                byte[] inputByte = new byte[len];     
                int length;
                long sum = 0;
                while ((length = isFromServer.read(inputByte, 0, inputByte.length)) > 0) {  
                    fos.write(inputByte, 0, length);  
                    fos.flush();
                    sum += length;	//�����Ż������˶��ļ�����Ƚ����ж�
                }
                fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int j = 0;j < filenum;j++) {
			filepath_info = filepath_info + filepath_arr[j] + "##";
		}
		filepath_info = filepath_info.substring(0, filepath_info.length() - 2);
		return true;
    }
    
    /*�õ�ʵ�鱣�������������ϸ��Ϣ*/
	public String getReturnDetail() {
		return detail;
	}
	/*�õ�ʵ�鱣�������������ϸ��Ϣ*/
	public String getFilePathInfo() {
		return filepath_info;
	}
}

package cn.org.bjca;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

/***************************************************************************
 * <pre>签章http接口调用demo</pre>
 * @文件名称:  HttpClientESSPDFTestDemo.java
 * @包   路   径：  cn.org.bjca.seal.esspdf.client.test
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人： lyr
 * @创建时间：2018-11-20 下午4:38:37
 ***************************************************************************/
public class HttpClientESSPDFTestDemo {



    // body内容长度定义,不用改变
    private static final int CONTEXT_LENGTH_SIZE = 4;
    //请求地址
    private String serverUrl = "http://192.168.114.166:8094/services/pdfServer";
    private String responseHead="";

    /**
     * 3.12.5.1	客户端自定义签章规则进行签章（关键字定位）
     * @throws Exception
     */
    @Test
    public void testPdfSignByKeyword() throws Exception {
        //待处理PDF
        byte[] bytes =FileUtils.readFileToByteArray(new File("D:\\test.pdf"));
        //签名图片(base64)
        String imageBase64 =  Base64.encodeBase64String(FileUtils.readFileToByteArray(new File("D:\\test.gif")));
        //头内容
        String headStr = "{\n" +
                "\t\"type\": \"31\",\n" +
                "\t\"ruleNum\": \"53C130E7BF352E1F\",\n" +
                "\t\"templateNum\": \"false\",\n" +
                "\t\"contents\": \"的##1##1##0##0\",\n" +
                "\t\"sealWidth\": 0.0,\n" +
                "\t\"sealHeight\": 0.0,\n" +
                "\t\"sealImg\": \""+imageBase64+"\",\n" +
                "\t\"pdfHashSignature\": false\n" +
                "}\n";
        //数据拼接合并
        byte[] data = mergeData(headStr.getBytes(),bytes);
        byte[] send = send(serverUrl, data);
        System.out.println("请求结果" + responseHead);
        if ("200".equals(responseHead)){
            FileUtils.writeByteArrayToFile(new File("D:\\pdfSignByKeyWordHttp1.pdf"),send);
        }
    }

    /**
     * 3.12.5.2	多规则签章
     * @throws Exception
     */
    @Test
    public void testPdfSignByRuleNums() throws Exception {
        //待处理PDF
        byte[] bytes =FileUtils.readFileToByteArray(new File("D:\\test.pdf"));
        //头内容
        String headStr = "{\n" +
                "\t\"ruleNum\": \"780901B9CF580C51\",\n" +
                "\t\"ruleNumList\": [\n" +
                "\t\t\"53582C51C506201E\",\n" +
                "\t\t\"6DB1D1F340D41E92\"\n" +
                "\t],\n" +
                "\t\"type\": \"1\"\n" +
                "}";
        //数据拼接合并
        byte[] data = mergeData(headStr.getBytes(),bytes);
        byte[] send = send(serverUrl, data);
        System.out.println("请求结果" + responseHead);
        if ("200".equals(responseHead)){
            FileUtils.writeByteArrayToFile(new File("D:\\testPdfSignByRuleNums.pdf"),send);
        }
    }

    /**
     * 3.12.5.3	组合规则签章
     * @throws Exception
     */
    @Test
    public void testFileBatchSignByParam() throws Exception {
        //待处理PDF
        byte[] bytes =FileUtils.readFileToByteArray(new File("D:\\test.pdf"));
        //头内容
        String headStr = "{\n" +
                "\t\"type\": \"1\",\n" +
                "\t\"ruleNum\": \"780901B9CF580C51 \",\n" +
                "\" serverSealNum \": \"4D4E8D3EE58DAC00\",\n" +
                "\" signPolicyNum \": \" 53C130E7BF352E1F 5E9217 \"\n" +
                "}";
        //数据拼接合并
        byte[] data = mergeData(headStr.getBytes(),bytes);
        byte[] send = send(serverUrl, data);
        System.out.println("请求结果" + responseHead);
        if ("200".equals(responseHead)){
            FileUtils.writeByteArrayToFile(new File("D:\\testPdfSignByRuleNums.pdf"),send);
        }
    }

    /**
     *3.12.5.4	多页签章
     * @throws Exception
     */
    @Test
    public void testPdfMultiPageSign() throws Exception {
        //待处理PDF
        byte[] bytes =FileUtils.readFileToByteArray(new File("D:\\test.pdf"));
        //头内容
        String headStr = "{\"type\": \"23\",\"ruleNum\": \"53582C51C506201E\"}";
        //数据拼接合并
        byte[] data = mergeData(headStr.getBytes(),bytes);
        byte[] send = send(serverUrl, data);
        System.out.println("请求结果" + responseHead);
        if ("200".equals(responseHead)){
            FileUtils.writeByteArrayToFile(new File("D:\\testPdfSignByRuleNums.pdf"),send);
        }
    }



    /**
     * 根据客户端关键字参数进行签章添加时间标记
     * @throws Exception
     */
    @Test
    public void testPdfSignByKeywordToAddTimeTag() throws Exception {
        //头内容
        String headStr = "{\n"
            + "\t\"type\": \"31\",\n"
            + "\t\"ruleNum\": \"53C130E7BF352E1F\",\n"
            + "\t\"templateNum\": \"true\",\n"
            + "\t\"contents\": \"合同##1##1##0##0\",\n"
            + "\t\"sealWidth\": 0.0,\n"
            + "\t\"sealHeight\": 0.0,\n"
            + "\t\"sealImg\": \"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\",\n"
            + "\t\"pdfHashSignature\": false,\n"
            + "\t\"taskID\": \"123b6bca4442451c9488da6db8d69b51\",\n"
            + "\t\"authorization\": {\n"
            + "\t\t\"authType\": \"OAUTH\",\n"
            + "\t\t\"appId\": \"AGLIQQZ5HN1JALQ3\",\n"
            + "\t\t\"secretKey\": \"bf6c49e8fd97688cae48a5294dee6955d84f261ca543e3f4990ec08ee52836f8\"\n"
            + "\t},\n"
            + "\t\"timeTag\": {\n"
            + "\t\t\"format\": \"yyyy年MM月dd日 HH时mm分ss秒\",\n"
            + "\t\t\"pos\": \"2\",\n"
            + "\t\t\"fontStyles\": \"SIMSUN\",\n"
            + "\t\t\"fontColor\": \"black\",\n"
            + "\t\t\"bold\": true,\n"
            + "\t\t\"fontSize\": 12,\n"
            + "\t\t\"unit\": \"pt\",\n"
            + "\t\t\"xoffset\": 0,\n"
            + "\t\t\"yoffset\": 0\n"
            + "\t}\n"
            + "}";
        //待处理PDF
        byte[] fileBty = FileUtils.readFileToByteArray(new File("/Users/Ryan.wang/bjca/anysign-parent-v2.1.0/anysign-client/src/test/resources/pdf/test1.pdf"));
        //数据拼接合并
        byte[] data = mergeData(headStr.getBytes(),fileBty);
        byte[] send = send(serverUrl, data);
        System.out.println("请求结果" + responseHead);
        if ("200".equals(responseHead)){
            FileUtils.writeByteArrayToFile(new File("/Users/Ryan.wang/bjca/anysign-parent-v2.1.0/anysign-client/src/test/resources/pdf/test111.pdf"),send);
        }
    }


    /**
     * 验签
     * @throws Exception
     */
    @Test
    public void testVerifiySign() throws Exception {
        //待处理PDF
        byte[] bytes =FileUtils.readFileToByteArray(new File("D:\\testPdfSignByRuleNums.pdf"));
        //头内容
        String headStr = "{\"type\": \"2\"}";
        //数据拼接合并
        byte[] data = mergeData(headStr.getBytes(),bytes);
        byte[] send = send(serverUrl, data);
        System.out.println("请求结果" + responseHead);
        if ("200".equals(responseHead)){
            FileUtils.writeByteArrayToFile(new File("D:\\verifiy.text"),send);
        }
    }


    /**
     *
     * @param reqBty headStr
     * @param pdfBty pdf
     * @return byte
     */
    private byte[] mergeData(byte[] reqBty,byte[] pdfBty) {
        byte[] headLengthBty = this.intToByte(reqBty.length);
        byte[] headBty = this.byteToByte(reqBty);
        byte[] bodyLengthBty = this.intToByte(pdfBty.length);
        byte[] bodyBty = pdfBty;
        byte[] allBty = new byte[headLengthBty.length + headBty.length + bodyLengthBty.length + bodyBty.length];
        System.arraycopy(headLengthBty, 0, allBty, 0, headLengthBty.length);
        System.arraycopy(headBty, 0, allBty, headLengthBty.length, headBty.length);
        System.arraycopy(bodyLengthBty, 0, allBty, headLengthBty.length + headBty.length, bodyLengthBty.length);
        System.arraycopy(bodyBty, 0, allBty, headLengthBty.length + headBty.length + bodyLengthBty.length, bodyBty.length);
        return allBty;
    }

    /**
     * 类型转换
     * @param inData byte
     * @return int
     */

    private int byteToInt(byte[] inData) {
        ByteBuffer byteBuf = ByteBuffer.allocate(4);
        byteBuf.put(inData);
        int outData = byteBuf.getInt(0);
        return outData;
    }

    private byte[] intToByte(int inData) {
        ByteBuffer byteBuf = ByteBuffer.allocate(4);
        byteBuf.putInt(inData);
        byte[] outData = byteBuf.array();
        return outData;
    }

    private byte[] byteToByte(byte[] headData) {
        ByteBuffer byteBuf = ByteBuffer.allocate(headData.length);
        byteBuf.put(headData);
        byte[] outData = byteBuf.array();
        return outData;
    }

    /**
     * 请求服务
     * @param serverUrl 请求地址
     * @param bty 数据
     * @return byte
     * @throws Exception 异常
     */
    private byte[] send(String serverUrl, byte[] bty) throws Exception {
        OutputStream output = null;
        InputStream is = null;
        byte[] pdfBty = null;
        byte[] headBty = null;
        try{
            URL url = new URL(serverUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "text/xml");
            httpConn.setRequestProperty("Content-Length", String.valueOf(bty.length));
            httpConn.setConnectTimeout(30000);
            httpConn.setReadTimeout(30000);
            httpConn.connect();
            output = httpConn.getOutputStream();
            output.write(bty);
            output.flush();
            // 写入请求参数
            int code = httpConn.getResponseCode();
            // 读取响应内容
            if (code == 200) {
                is = httpConn.getInputStream();
                byte[] bb = null;// head(4)+bodylength(4) = 8
                int n = 0;
                int headLength = 0;
                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        bb = new byte[CONTEXT_LENGTH_SIZE];
                        is.read(bb, 0, CONTEXT_LENGTH_SIZE);
                        n = byteToInt(bb);
                        headLength = n;
                    } else if (i == 1) {
                        bb = new byte[headLength];
                        is.read(bb, 0, headLength);
                        responseHead=new String(bb);
                    } else if (i == 2) {
                        bb = new byte[CONTEXT_LENGTH_SIZE];
                        is.read(bb, 0, CONTEXT_LENGTH_SIZE);
                        n = byteToInt(bb);
                    }
                }
                if (n > 0) {
                    byte[] bty2 = new byte[n];
                    int offset = 0;
                    while (true) {
                        int len = is.read(bty2, offset, n - offset);
                        if (len + offset == n) {
                            break;
                        }
                        offset = len + offset;
                    }
                    pdfBty = bty2;
                }
            } else {
                String sTotalString = "远程服务器连接失败,错误代码:" + code;
                System.out.println("response:" + sTotalString);
            }
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(output);
        }
        return pdfBty;
    }

}

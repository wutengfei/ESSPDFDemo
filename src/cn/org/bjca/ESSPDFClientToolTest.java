package cn.org.bjca;

import cn.org.bjca.seal.esspdf.client.enumeration.EnumFontFamily;
import cn.org.bjca.seal.esspdf.client.message.*;
import cn.org.bjca.seal.esspdf.client.tools.AnySignClientTool;
import cn.org.bjca.seal.esspdf.client.utils.ClientUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/***************************************************************************
 * <pre>PDF签章服务器客户端工具测试类</pre>
 * @文件名称: ESSPDFClientToolTest.java
 * @包 路   径：  cn.org.bjca.seal.esspdf.client.test
 * @版权所有：北京数字认证股份有限公司 (C) 2013
 *
 * @类描述: 
 * @版本: V2.0
 * @创建人： wangzhijun
 * @创建时间：2018-10-31 下午1:34:10
 ***************************************************************************/
public class ESSPDFClientToolTest {

    AnySignClientTool anySignClientTool = null;

    private String resourcesPath;
    String testPdfFilePath;
    String testXMLFilePath;
    String testSignedPdfFilePath;
    String testOfdFilePath;
    String testSignedOfdFilePath;


    @Before
    public void init() throws Exception {
        String ip = "223.70.139.221";
//        String ip = "127.0.0.1";
        int port = 8888;
        anySignClientTool = new AnySignClientTool(ip, port);
        //客户端软负载
//      anySignClientTool = new AnySignClientTool("127.0.0.1:8888","192.168.126.148:8888");
        //连接服务器超时时间，单位是毫秒，默认为5秒，当前为50秒
        anySignClientTool.setTimeout(50000);//连接超时时间设置
        anySignClientTool.setRespTimeout(50000);//响应超时时间

        resourcesPath = URLDecoder.decode(this.getClass().getClassLoader().getResource("").getPath(), "UTF-8");
        testPdfFilePath = resourcesPath + "pdf" + File.separator + "test.pdf";
        testXMLFilePath = resourcesPath + "xml" + File.separator + "test.xml";
        testSignedPdfFilePath = resourcesPath + "out" + File.separator + "signedPDF.pdf";
        testOfdFilePath = resourcesPath + "ofd" + File.separator + "test.ofd";
        testSignedOfdFilePath = resourcesPath + "out" + File.separator + "signedOFD.ofd";
    }
    
    @Test
	public void testPdfSign() throws Exception {//PDF单规则签章
		System.out.println("*******************");
		byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		String ruleNum = "F34848AA7BA92";//签章规则编号
		long begin = System.currentTimeMillis();
		ChannelMessage message = anySignClientTool.pdfSign(ruleNum, pdfBty);
		long end = System.currentTimeMillis();
		System.out.println("********************运行时间:"+(end - begin)/1000f+"s");
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){//成功
			ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
		}
	}
    
    @Test
    public void testPdfSign_sealNum_policyNum() throws Exception {//PDF组合规则签章,自定义服务印章编号、签名策略编号
        System.out.println("*******************");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String ruleNum = "F34848AA7BA92";//签章规则编号
        String serverSealNum = "53875D2CD6B82EEF";//服务器印章编号
        String signPolicyNum = "54168A90A9006D57";//签名策略编号
        ChannelMessage message = anySignClientTool.pdfSign(ruleNum, serverSealNum, signPolicyNum, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testPdfSignList() throws Exception {//PDF多规则签章
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        List<String> ruleNumList = new ArrayList<String>();//
        String ruleNum = "F34848AA7BA92";//签章规则编号列表
        String ruleNum2 = "5458FB5D47371229";//签章规则编号列表
        String ruleNum3 = "62F887B9A2D55AE1";//签章规则编号列表
        ruleNumList.add(ruleNum);
        ruleNumList.add(ruleNum2);
        ruleNumList.add(ruleNum3);
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.pdfSign(ruleNumList, pdfBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testPdfSpeedSign() throws Exception {//PDF高速签章
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        List<String> ruleNumList = new ArrayList<String>();//
        String ruleNum = "F34848AA7BA92";//签章规则编号列表
        String ruleNum2 = "5458FB5D47371229";//签章规则编号列表
        String ruleNum3 = "62F887B9A2D55AE1";//签章规则编号列表
        ruleNumList.add(ruleNum);
        ruleNumList.add(ruleNum2);
        ruleNumList.add(ruleNum3);
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.pdfSpeedSign(ruleNumList, pdfBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testPdfVerify() throws Exception {//PDF验签测试
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testSignedPdfFilePath));//pdf字节数组
        String orgCode = "123456";//机构代码
        ChannelMessage message = anySignClientTool.pdfVerify( pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
    }

    @Test
    public void testPdfVerifyForNoOrgCode() throws Exception {//PDF验签测试
        System.out.println("******************123");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        ChannelMessage message = anySignClientTool.pdfVerify(pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        } else {
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        }
    }

    @Test
    public void testPdfFormVerify() throws Exception {
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String type = "24"; // 验证空签名域签名有效性，未签名的空签名域和非Adobe Reader进行的签名都返回验证失败
        ChannelMessage message = anySignClientTool.pdfFormVerify(pdfBty, type);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        } else {
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        }
    }

    @Test
    public void testPdfVerifyWithAnysign() throws Exception {//PDF验签测试
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        ChannelMessage message = anySignClientTool.pdfVerifyWithAnysign(pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        } else {
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        }
    }
    
    /**
     * PDF插入空签名域
     */
    @Test
    public void testGenSignField() throws Exception {
        System.out.println("******************123");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.genSignField("17CD5188387EF54B", null, pdfBty);
        long end = System.currentTimeMillis();
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println(testSignedPdfFilePath);
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }
    @Test
    public void testGenPDFDocument() throws Exception {//根据模版生成PDF文档
        System.out.println("******************1");
//        byte[] dataBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));//xml模版数据
        byte[] dataBty = ClientUtil.readFileToByteArray(new File("F:\\BJCA\\product\\PDF模板培训\\PDF模板培训\\List多重循环与assign的使用实例.xml"));//xml模版数据
        String templateNum = "5C274232B4A3987";//模版编号
        ChannelMessage message = anySignClientTool.genPDFDocument(templateNum, dataBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println(testSignedPdfFilePath);
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testGenPDFDocumentAndSign() throws Exception {//根据模版生成PDF文档且签章
        byte[] dataBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));//xml模版数据
        String templateNum = "CA253CD9085C3E6";//模版编号
        String ruleNum = "0132sheshui##0224sheshui";//签章规则编号
        ChannelMessage message = anySignClientTool.genPDFDocumentAndSign(templateNum, ruleNum, dataBty);
        System.out.println("状态码：" + message.getStatusCode());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testGenPDFDocumentAndSigns() throws Exception {//根据模版生成PDF文档且多规则签章
        System.out.println("***************1");
        byte[] dataBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));//xml模版数据
        String templateNum = "336D77CF3BB8531A";//模版编号
        List<String> ruleNumList = new ArrayList<String>();
        String ruleNum = "473F63381A931BFE";//签章规则编号列表
        ruleNumList.add(ruleNum);
        ruleNum = "2400608F35D122D5";//签章规则编号列表
        ruleNumList.add(ruleNum);
        ChannelMessage message = anySignClientTool.genPDFDocumentAndSign(templateNum, ruleNumList, dataBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("结果信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testGenBarcodeAndPdf() throws Exception {//产生条形码+pdf
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String ruleNum = "58C00407580D99E9";
        String contents = new String(ClientUtil.readFileToByteArray(new File(testXMLFilePath)));
        ChannelMessage message = anySignClientTool.genBarcode(ruleNum, contents, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testGenBarcodeAndPdfAndSign() throws Exception {//产生条形码+pdf+签章
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String contents = "123456";//new String(ClientUtil.readFileToByteArray(new File("D:/renbaoshou/1111.xml")));
        String barcodeRuleNum = "AB03B44D1702A1C"; // 58C00407580D99E9
        String signSealRuleNum = "6FE908DFDEAF8C3C"; // 10F70D0942747AB5
        ChannelMessage message = anySignClientTool.genBarcodeAndSign(barcodeRuleNum, signSealRuleNum, contents, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testGenBarcodeAndXML() throws Exception {//产生条形码+XML+DOCX
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));//xml字节数组
        String ruleNum = "34FE8CB40C30B43E";
        ChannelMessage message = anySignClientTool.genBarcode(ruleNum, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testgGenBarcodeAndXMLAndSign() throws Exception {//产生条形码+XML+DOCX+签章
        byte[] xmlBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));//xml字节数组
        String barcodeRuleNum = "AB03B44D1702A1C";
        String signSealRuleNum = "53582C51C506201E";
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.genBarcodeAndSign(barcodeRuleNum, signSealRuleNum, xmlBty);
        long end = System.currentTimeMillis();
        System.out.println("The running time:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testGenBarcodeImageWithXML() throws Exception {//产生条形码图片
        byte[] xmlBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));//xml字节数组
        String ruleNum = "4C2C6D118541AAF8";
        ChannelMessage message = anySignClientTool.genBarcodeImage(ruleNum, xmlBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File("C:/Users/admin/Desktop/qr.png"), message.getBody());
        }
    }

    @Test
    public void testGenBarcodeImageWithText() throws Exception {//产生条形码图片
        String content = "ABCDEFG0987651";
        String ruleNum = "3438021E306FDB43";
        ChannelMessage message = anySignClientTool.genBarcodeImage(ruleNum, content.getBytes("UTF-8"));
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File("D:/qr2.png"), message.getBody());
        }
    }

    @Test
    public void testExportXmlData() throws Exception {//从PDF导出xml模板数据
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//PDF文件
        ChannelMessage message = anySignClientTool.exportXMLData(pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File("C:/testXmlData.xml"), message.getBody());
        }
    }

    @Test
    public void testPdfSignByWord() throws Exception {//word转PDF进行签章测试
        byte[] wordBty = ClientUtil.readFileToByteArray(new File("C:/Users/DSVS接口测试程序使用指南.doc"));//word字节数组
        String ruleNum = "10F70D0942747AB5";//签章规则编号
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.pdfSignByWord(ruleNum, wordBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File("C:\\Users\\admin\\Desktop\\pdf to image\\DSVS接口测试程序使用指南.doc"), message.getBody());
        }
    }

    @Test
    public void testGenPDFInvoiceAndSign() throws Exception {//根据票据模版产生PDF文档且签名，杭州第一人民医院定制
        byte[] dataBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));//xml模版数据
        String templateNum = "78C0C9EFB64AC923";//模版编号
        String ruleNum = "1A5E5D2447656A20";//签章规则编号
        String fileName = "20140505_31441927_14111";
        ChannelMessage message = anySignClientTool.genPDFInvoiceAndSign(templateNum, ruleNum, fileName, dataBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
    }
    
    /**
     * 产生客户端PDF签名摘要数据支持插入拓展信息
     * v2.0.5 新增
     *
     * @throws Exception
     */
    @Test
    public void testGenClientSignDigestExt() throws Exception {//产生客户端PDF签名摘要数据支持插入拓展信息
        List<ClientSignMessage> clientSignMessages = new ArrayList<ClientSignMessage>();
        ClientSignMessage clientSignMessage = new ClientSignMessage();
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String keyword = "北京";
        // RSA
        String signCert = "MIIFPTCCBCWgAwIBAgIKGzAAAAAAAFDMPTANBgkqhkiG9w0BAQUFADBSMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMTAeFw0xOTA0MTYxNjAwMDBaFw0yMDA1MTQxNTU5NTlaMIGPMQswCQYDVQQGEwJDTjEtMCsGA1UECgwk5YyX5Lqs5pWw5a2X6K6k6K+B6IKh5Lu95pyJ6ZmQ5YWs5Y+4MQowCAYDVQQLDAExMRIwEAYDVQQDDAnnjovlv5flhpsxJTAjBgkqhkiG9w0BCQEWFndhbmd6aGlqdW5AYmpjYS5vcmcuY24xCjAIBgNVBAwMATEwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAKPKcrJBO1xadFQzIu7q4AHF9k2pS5rjCSg5c1w58zsg+phFNxcnCvFRyCeik47bE1Vs9yOIl1lgAKc2mJ1TJIhfxiWV5HwPlecNeNAGXi1qW34t3NDuqwDj7BJ/6dpj4RKChc5F1SgM645CZZzBlO8L/OGc1PvVvzfllKBpJDfpAgMBAAGjggJZMIICVTAfBgNVHSMEGDAWgBSsO+yvDKNQDu+vr7RPbDvb0VfSiTAdBgNVHQ4EFgQUYKfKOxWhWTmOpUcxsFRwws5dCJ0wCwYDVR0PBAQDAgbAMIGvBgNVHR8EgacwgaQwbaBroGmkZzBlMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMTERMA8GA1UEAxMIY2EzY3JsMjcwM6AxoC+GLWh0dHA6Ly9sZGFwLmJqY2Eub3JnLmNuL2NybC9wdGNhL2NhM2NybDI3LmNybDAJBgNVHRMEAjAAMBEGCWCGSAGG+EIBAQQEAwIA/zAdBgUqVgsHAQQUU0YxMzA4MjYxOTg3MDcyMzIxMTYwHQYFKlYLBwgEFFNGMTMwODI2MTk4NzA3MjMyMTE2MCAGCGCGSAGG+EQCBBRTRjEzMDgyNjE5ODcwNzIzMjExNjAbBggqVoZIAYEwAQQPMTAyMDgwMDAzMDY5MTIzMCUGCiqBHIbvMgIBBAEEFzJDQFNGMTMwODI2MTk4NzA3MjMyMTE2MCoGC2CGSAFlAwIBMAkKBBtodHRwOi8vYmpjYS5vcmcuY24vYmpjYS5jcnQwDwYFKlYVAQEEBjEwMDAwMDBABgNVHSAEOTA3MDUGCSqBHIbvMgICATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vd3d3LmJqY2Eub3JnLmNuL2NwczATBgoqgRyG7zICAQEeBAUMAzUyMDANBgkqhkiG9w0BAQUFAAOCAQEAc+8U+Rerl7gGdj6e8lzVUsQ83ux6PN1xCcfB4wqDQFctWh4oOIQqFW6PW+S709Ie7gnDk9MuuIA397DbYl8DGDQL1LJqEJ+QzURSIU2St3yusi6G+t1fCDWxL8EwJjwz+jjWXiKiLnfVnWVQQo/E2Bji83TjLZ0+/NF5Cnyh5QdWW3RnZ1O7SVQ2JFbKLDkJVlm+Xpi7TGVFROjOIy7UNVz0BS8j6Z6Lu4LQeiXENmWcNeBQ6Iu0eRR4nrgQgGMlB+gSNNavsSbCtY21IaZfOSQZ+ub4p+E517/TujkMMM/uEo8yqtCK3XGHuASUZ4uN14DrE9naX+5oOiSWr5kmJw==";
        String sealImg = "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";
        float sealWidth = 100;
        float sealHeight = 100;
        String moveType = "3";
        String searchOrder = "0";
        clientSignMessage.setKeyword(keyword);
        clientSignMessage.setMoveType(moveType);
        clientSignMessage.setSearchOrder(searchOrder);
        clientSignMessage.setPdfBty(pdfBty);
        clientSignMessage.setFileUniqueId("111111111111");
        //上下偏移
//		clientSignMessage.setHeightMoveSize(10);
        //左右偏移
        clientSignMessage.setMoveSize(300);
        clientSignMessages.add(clientSignMessage);

        ExtentionBean extBean = new ExtentionBean();
        extBean.setOnSealText("wangzhijun");
        extBean.setFontFamily(EnumFontFamily.FONTFAMILY_1.name());
        extBean.setFontSize("12");
        extBean.setFontColor("#FF0000");
//    	extBean.setTimeFromat(TimeTagFromat.CHINESE_FORMAT.name());// 只有sealImg设置为0时设置该参数，生成制定日期格式的图片
        ChannelMessage message = anySignClientTool.genClientSignDigest(clientSignMessages, signCert, sealImg, sealWidth, sealHeight, extBean);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if (message.getStatusCode().equals("200")) {
            ClientUtil.writeByteArrayToFile(new File("D:/json.txt"), message.getBody());
        }
    }
    
    @Test
    public void testGenClientSignDigest() throws Exception {//产生客户端PDF签名摘要数据
        List<ClientSignMessage> clientSignMessages = new ArrayList<ClientSignMessage>();
        ClientSignMessage clientSignMessage = new ClientSignMessage();
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String keyword = "main";
        // SM2
//    	String signCert = "MIIErDCCBFOgAwIBAgIKLRAAAAAAAAVaujAKBggqgRzPVQGDdTBEMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0EwHhcNMTUwMjA0MTYwMDAwWhcNMTYwMjA1MTU1OTU5WjBoMRQwEgYDVQQpDAsxMzI5ODc2NTQzMjEdMBsGA1UEAwwU5rWL6K+V6K+B5LmmKOa1i+ivlSkxDTALBgNVBAoMBEJKQ0ExFTATBgNVBAoMDOa1i+ivleivgeS5pjELMAkGA1UEBgwCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATpV4nYVfxwzjxtnyng6qrwO/evH5W4TiDQKaqZnV7OWY/2ZMwMVtf2Pc3fLfUQvQ/E4Xf3eN8QNRtcdLm8/ho0o4IDBzCCAwMwHwYDVR0jBBgwFoAUH+bP1I/FIiqXSimKFecWyZI0xLYwHQYDVR0OBBYEFHKqmJobumZM9dxXIJdMAAODEKxzMAsGA1UdDwQEAwIGwDCBmwYDVR0fBIGTMIGQMF+gXaBbpFkwVzELMAkGA1UEBhMCQ04xDTALBgNVBAoMBEJKQ0ExDTALBgNVBAsMBEJKQ0ExFzAVBgNVBAMMDkJlaWppbmcgU00yIENBMREwDwYDVQQDEwhjYTIxY3JsMjAtoCugKYYnaHR0cDovL2NybC5iamNhLm9yZy5jbi9jcmwvY2EyMWNybDIuY3JsMB0GCiqBHIbvMgIBAQEEDwwNSkoxMzI5ODc2NTQzMjBgBggrBgEFBQcBAQRUMFIwIwYIKwYBBQUHMAGGF09DU1A6Ly9vY3NwLmJqY2Eub3JnLmNuMCsGCCsGAQUFBzAChh9odHRwOi8vY3JsLmJqY2Eub3JnLmNuL2NhaXNzdWVyMGwGA1UdIARlMGMwMAYDVR0gMCkwJwYIKwYBBQUHAgEWGyBodHRwOi8vd3d3LmJqY2Eub3JnLmNuL2NwczAvBgNVHSAwKDAmBggrBgEFBQcCARYaaHR0cDovL3d3dy5iamNhLm9yZy5jbi9jcHMwEQYJYIZIAYb4QgEBBAQDAgD/MBsGCiqBHIbvMgIBAQgEDQwLMTMyOTg3NjU0MzIwHQYKKoEchu8yAgECAgQPDA1KSjEzMjk4NzY1NDMyMB8GCiqBHIbvMgIBAQ4EEQwPOTk5MDAwMTAwMDA2MDgzMB0GCiqBHIbvMgIBAQQEDwwNSkoxMzI5ODc2NTQzMjAsBgoqgRyG7zICAQMBBB4MHDIxMzAzMDAwMDEyMzQ1Njc4OTAwOTg3NjU0MzIwJgYKKoEchu8yAgEBFwQYDBYxQDIxNTAwOUpKMDEzMjk4NzY1NDMyMCwGCiqBHIbvMgIBARoEHgwcMjEzMDMwMDAwMTIzNDU2Nzg5MDA5ODc2NTQzMjAUBgoqgRyG7zICAQEeBAYMBDEwMTgwCgYIKoEcz1UBg3UDRwAwRAIgEOPcgOL1lt5Pud/GJoQsaDIkDbn5ULmanop94Rcx1YoCIFrhXB//Me1g2kcM7r4DhKmveaXFU/mjsceJVmWHg2GU";
        // RSA
        String signCert = "MIIFpTCCBI2gAwIBAgIKGzAAAAAAAAiYZTANBgkqhkiG9w0BAQUFADBSMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMTAeFw0xNDEwMTkxNjAwMDBaFw0xNTEwMjAxNTU5NTlaMGcxCzAJBgNVBAYTAkNOMRIwEAYDVQQDDAnnjovmlofmmIwxJzAlBgkqhkiG9w0BCQEWGHdhbmd3ZW5jaGFuZ0BiamNhLm9yZy5jbjEbMBkGCgmSJomT8ixkASkMCzEzMzcwMTA5MTMyMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLvm9GDuGaQou9FznmE/NGlwTkVi/1E7JbfOq9g1kmwNOJgNgwxilhQOl04Y78jmYRv2Pw+ZLuRFz0Fxne6EpU7cQ4imqI53VDyFUbydQNRJoU/BoQnOzmDnMsjk7wot2EqBniGRfPQxX3zvlble/nqBT7aKvWWONJPkS8+T+PlwIDAQABo4IC6jCCAuYwHwYDVR0jBBgwFoAUrDvsrwyjUA7vr6+0T2w729FX0okwHQYDVR0OBBYEFOlR0ojlKw3yE4y5nhSwu1tx7xIcMAsGA1UdDwQEAwIGwDCBrQYDVR0fBIGlMIGiMGygaqBopGYwZDELMAkGA1UEBhMCQ04xDTALBgNVBAoMBEJKQ0ExGDAWBgNVBAsMD1B1YmxpYyBUcnVzdCBDQTEaMBgGA1UEAwwRUHVibGljIFRydXN0IENBLTExEDAOBgNVBAMTB2NhM2NybDMwMqAwoC6GLGh0dHA6Ly9sZGFwLmJqY2Eub3JnLmNuL2NybC9wdGNhL2NhM2NybDMuY3JsMAkGA1UdEwQCMAAwEQYJYIZIAYb4QgEBBAQDAgD/MB0GBSpWCwcBBBRTRjMyMDcyNDE5ODAwNDI1MjcxNjAdBgUqVgsHCAQUU0YzMjA3MjQxOTgwMDQyNTI3MTYwIAYIYIZIAYb4RAIEFFNGMzIwNzI0MTk4MDA0MjUyNzE2MBsGCCpWhkgBgTABBA8xMDIwMDAwMDA3Mzk5NjMwJQYKKoEchu8yAgEEAQQXM0NAU0YzMjA3MjQxOTgwMDQyNTI3MTYwKgYLYIZIAWUDAgEwCQoEG2h0dHA6Ly9iamNhLm9yZy5jbi9iamNhLmNydDAPBgUqVhUBAQQGMTAwMDgwMIHnBgNVHSAEgd8wgdwwNQYJKoEcAcU4gRUBMCgwJgYIKwYBBQUHAgEWGmh0dHA6Ly93d3cuYmpjYS5vcmcuY24vY3BzMDUGCSqBHAHFOIEVAjAoMCYGCCsGAQUFBwIBFhpodHRwOi8vd3d3LmJqY2Eub3JnLmNuL2NwczA1BgkqgRwBxTiBFQMwKDAmBggrBgEFBQcCARYaaHR0cDovL3d3dy5iamNhLm9yZy5jbi9jcHMwNQYJKoEcAcU4gRUEMCgwJgYIKwYBBQUHAgEWGmh0dHA6Ly93d3cuYmpjYS5vcmcuY24vY3BzMA0GCSqGSIb3DQEBBQUAA4IBAQCyKW4oi7Ydh7y88/fVd3F1+PnsNWclJsHZ3Vu+jdYtoWeFGw36wX/FZAyv51UjsqMErB8jwoKkKnaP7PvlZrm2tXkQ06QthR3Hg3hhmAcQM8FT9/V99E18XMei7vDjymgFYTI9P/36sLjE7ORRuLn9VQUEbBKBht/Ifp3ge0cw38Nz2xv9t68Mb7UKD403B8gto+jAARDDRIZtoMlWHqpTfewKOndzohgEsPpzQXP1y4bC1WFQQBKSamveTouOhui/KKOesOKyOF/cVhvq/ilv04mfYYpK0XMiXB2047hwYWpZAHwVncyd62XC2GfpDYVjvorYzVuVLi5BrkIx8ExA";

        String sealImg = "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";
        float sealWidth = 100;
        float sealHeight = 100;
        String moveType = "3";
        String searchOrder = "2";
        clientSignMessage.setKeyword(keyword);
        clientSignMessage.setMoveType(moveType);
        clientSignMessage.setSearchOrder(searchOrder);
        clientSignMessage.setPdfBty(pdfBty);
        clientSignMessage.setFileUniqueId("111111111111");
        //上下偏移
//		clientSignMessage.setHeightMoveSize(10);
        //左右偏移
        clientSignMessage.setMoveSize(300);
        clientSignMessages.add(clientSignMessage);
        clientSignMessage = new ClientSignMessage();
        clientSignMessage.setKeyword(keyword);
        clientSignMessage.setMoveType(moveType);
        clientSignMessage.setSearchOrder(searchOrder);
        clientSignMessage.setPdfBty(pdfBty);
        clientSignMessage.setFileUniqueId("222222");
        clientSignMessages.add(clientSignMessage);
        ChannelMessage message = anySignClientTool.genClientSignDigest(clientSignMessages, signCert, sealImg, sealWidth, sealHeight);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if (message.getStatusCode().equals("200")) {
            ClientUtil.writeByteArrayToFile(new File("D:/json.txt"), message.getBody());
        }
    }


    //产生客户端PDF签名
    @Test
    public void genClientSign() throws Exception {
        String json = "D:/test/json.txt";
        String signDataJson = new String(ClientUtil.readFileToByteArray(new File(json)));
        ChannelMessage message = anySignClientTool.genClientSign(signDataJson);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            List<ClientSignBean> signBeanList = message.getClientSignList();
            for (ClientSignBean beanObj : signBeanList) {
                ClientUtil.writeByteArrayToFile(new File("D:/" + beanObj.getSignUniqueId() + ".pdf"), beanObj.getPdfBty());
            }
        }
    }

    //doc、docx 转PDF ,且插入产生条形码+签章
    @Test
    public void testGenBarcodeAndSignByWord() throws Exception {
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File("C:/Users/admin/Desktop/海南地税/停业税务事项通知书（带附表）.doc"));//pdf字节数组
        String barcodeRuleNum = "1D6B823D900DA0B0";
        String signSealRuleNum = "B501FDFA15AFDFE";
        String contents = "C:/Users/admin/Desktop/停业税务事项通知书（带附表）.pdf";
        ChannelMessage message = anySignClientTool.genBarcodeAndSignByWord(barcodeRuleNum, signSealRuleNum, contents, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }






    // 图片转PDF签章
    @Test
    public void testImagesToPDFSign() throws Exception {
        byte[] imageBty1 = ClientUtil.readFileToByteArray(new File("C:/2-200506160001.tif"));
        byte[] imageBty2 = ClientUtil.readFileToByteArray(new File("C:/2-200506160002.tif"));
        List<ClientSignMessage> signMessageList = new ArrayList<ClientSignMessage>();
        ClientSignMessage signMessage = new ClientSignMessage();
        signMessage.setImageBty(imageBty1);
        signMessageList.add(signMessage);
        signMessage = new ClientSignMessage();
        signMessage.setImageBty(imageBty2);
        signMessageList.add(signMessage);
        List<String> ruleNumList = new ArrayList<String>();//
        String ruleNum = "537CA483849D4EF2";//签章规则编号列表
        ruleNumList.add(ruleNum);
        ChannelMessage message = anySignClientTool.pdfSignByImages(ruleNumList, signMessageList);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    // 根据图片转PDF文件进行多页签章
    @Test
    public void testPdfMultiPageSignByImages() throws Exception {
        byte[] imageBty1 = ClientUtil.readFileToByteArray(new File("C:\\Users\\admin\\Desktop\\test-pdf\\2-200506160001.tif"));
        byte[] imageBty2 = ClientUtil.readFileToByteArray(new File("C:\\Users\\admin\\Desktop\\test-pdf\\2-200506160002.tif"));
        List<ClientSignMessage> signMessageList = new ArrayList<ClientSignMessage>();
        ClientSignMessage signMessage = new ClientSignMessage();
        signMessage.setImageBty(imageBty1);
        signMessageList.add(signMessage);
        signMessage = new ClientSignMessage();
        signMessage.setImageBty(imageBty2);
        signMessageList.add(signMessage);
        signMessage = new ClientSignMessage();
        signMessage.setImageBty(imageBty2);
        signMessageList.add(signMessage);
        signMessage = new ClientSignMessage();
        signMessage.setImageBty(imageBty2);
        signMessageList.add(signMessage);
        signMessage = new ClientSignMessage();
        signMessage.setImageBty(imageBty2);
        signMessageList.add(signMessage);
        //签章规则编号列表
        String ruleNum = "54236EE630F95598";
        ChannelMessage message = anySignClientTool.pdfMultiPageSignByImages(ruleNum, signMessageList, "北京数字认证");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    // PDF多页签章
    @Test
    public void testPdfMultiPageSign() throws Exception {
        System.out.println("*****************");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
        String ruleNum = "F34848AA7BA92";
        ChannelMessage message = anySignClientTool.pdfMultiPageSign(ruleNum, pdfBty, "");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }

    }


    //GBK转换成UTF-8
    @Test
    public void gbkConvertToUTF8() throws Exception {
        byte[] gbkBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
        byte[] utf8Bty = anySignClientTool.gbkConvertToUTF8(gbkBty);
        ClientUtil.writeByteArrayToFile(new File(testXMLFilePath), utf8Bty);
    }

    @Test
    public void testField() throws Exception {
        String ruleNum = "6D5EE1B50587FCA0";
        byte[] xml = ClientUtil.readFileToByteArray(new File(testXMLFilePath));
        ChannelMessage message = anySignClientTool.genSignFieldByXML(ruleNum, xml);
        System.out.println(message.getStatusCode());
        System.out.println(message.getStatusInfo());
        if (message.getStatusCode().equals("200")) {
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    /**
     * pdf添加水印
     * <p>testPdfAndWatermark</p>
     *
     * @throws Exception
     * @Description:
     */
    @Test
    public void testPdfAndWatermark() throws Exception {
        System.out.println("*****************");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
        String ruleNum = "5E9217F50C3E491D";
        byte[] xmlBty = ClientUtil.readFileToByteArray(new File(testXMLFilePath));
        String waterMarkTemplate = new String(xmlBty, "UTF-8");
        ChannelMessage message = anySignClientTool.pdfAndWatermark(ruleNum, pdfBty, "ESS-PDF-TEST", waterMarkTemplate);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    /**
     * 业务系统查询获取签章规则、服务器印章、签名策略、二维码规则的接口（查询条件：名称、规则编号、组织机构名称、组织机构编码）
     * <p>selectListByParamTest</p>
     *
     * @throws Exception
     * @Description:
     */
    @Test
    public void selectListByParamTest() throws Exception {//客户端传入名称和类型，查询出名称和编号json
        MessageBean reqmes = new MessageBean();
		/* 查询对象类型(证书列表:MessageBean.CERTLIST、印章列表:MessageBean.SERVERSEALLIST、
					签章规则列表:MessageBean.SIGNSEALRULELIST，二维码列表：MessageBean.BARCODERULELIST)*/
        reqmes.setObjectType(MessageBean.SERVERSEALLIST);
        //reqmes.setReq_Name("lyr测试");//根据名称查询
//        reqmes.setReq_Num("qf1");//根据编号查询
//        reqmes.setReq_OrgName("根机构");//查询签章规则查询设置参数（机构名称）
        reqmes.setReq_OrgNum("123456");
        ChannelMessage message = anySignClientTool.selectListByParam(reqmes);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        System.out.println("返回结果：" + new String(message.getBody(), "utf-8"));
    }

    /**
     * PDF签章插入附文测试
     * <p>testPdfSignFW</p>
     *
     * @throws Exception
     * @Description:
     */
    @Test
    public void testPdfSignFW() throws Exception {
        System.out.println("*******************12");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String ruleNum = "53582C51C506201E";//签章规则编号
        ExtentionBean extentionBean = new ExtentionBean();
        List<String> onSealTextList = new ArrayList<String>();
        onSealTextList.add("海淀区");
        onSealTextList.add("质保部");
        extentionBean.setOnSealTextList(onSealTextList);
        extentionBean.setxRate(10);
        extentionBean.setyRate(40);
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.pdfSign(ruleNum, pdfBty, extentionBean);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }

    @Test
    public void testGenerateSealImage() throws Exception {
        SignSealImageBean signSealImageBean = new SignSealImageBean();
        signSealImageBean.setTempId("999999");//
        signSealImageBean.setHeadWords("成都武侯维康美年大健康体检门诊部有限公司");
        signSealImageBean.setSubCenterWords("91510107MA62L5KN3UABCD");
        signSealImageBean.setMainCenterWords("发票专用章");
        ChannelMessage message = anySignClientTool.generateSealImage(signSealImageBean);
        System.out.println(message.getStatusCode());
        System.out.println(message.getStatusInfo());
        if (message.getStatusCode().equals("200")) {
            FileUtils.writeByteArrayToFile(new File("E:/out_test6.png"), message.getBody());
        }
    }

    /**
     * OFD规则签章
     * @throws Exception
     */
    @Test
    public void testOfdRuleSign() throws Exception {
        //pdf字节数组
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testOfdFilePath));
        //签章规则编号
        String ruleNum = "7B38600AEF6111E8";
        long begin = System.currentTimeMillis();
        String taskId = UUID.randomUUID().toString();
        ChannelMessage message = anySignClientTool.ofdSign(taskId, ruleNum, pdfBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {
            ClientUtil.writeByteArrayToFile(new File(testSignedOfdFilePath), message.getBody());
        }
    }

    /**
     * OFD规则 + 印章编号
     * @throws Exception
     */
    @Test
    public void testOfdRuleAndSealNumSign() throws Exception {
        //pdf字节数组
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testOfdFilePath));
        //签章规则编号
        String ruleNum = "7B38600AEF6111E8";
        String serverSealNum = "GM_95d05b6968e30ca5f2d0d30c165fb01600c362e4";
        long begin = System.currentTimeMillis();
        String taskId = UUID.randomUUID().toString();
        ChannelMessage message = anySignClientTool.ofdSign(taskId, ruleNum, serverSealNum, pdfBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {
            ClientUtil.writeByteArrayToFile(new File(testSignedOfdFilePath), message.getBody());
        }
    }

    /**
     * OFD外传坐标签章
     * @throws Exception
     */
    @Test
    public void ofdSignByParamRectangle() throws Exception{
        //签章规则编号
        String serverSealNum = "GM_95d05b6968e30ca5f2d0d30c165fb01600c362e4";
        RectangleBean bean = new RectangleBean();
        //pdf字节数组
        byte[] ofdFileByte = ClientUtil.readFileToByteArray(new File(testOfdFilePath));
        bean.setPageNo(1); // 签章页码（0 全部页， 1 第一页 ……）
        bean.setLeft(40); // 印章距离OFD左边界
        bean.setTop(40); //  印章距离OFD上边界
        // 注意若为椭圆章，则印章以较长边等比例缩放
        bean.setSealWidth(0);// 印章宽度（若 宽度<=0 使用印章印章编号中记录的宽度 ）
        bean.setSealHeight(0); // 印章高度 （若 高度<=0 使用印章印章编号中记录的高度 ）
        String taskId = UUID.randomUUID().toString();
        ChannelMessage message = anySignClientTool.ofdSignByParamRectangle(taskId, ofdFileByte, serverSealNum, bean);
        System.out.println(message.getStatusCode());
        System.out.println(message.getStatusInfo());
        if (message.getStatusCode().equals("200")) {
            ClientUtil.writeByteArrayToFile(new File(testSignedOfdFilePath), message.getBody()); // 签章后的OFD文件字节数组
        }
    }

    /**
     * OFD外传关键字签章
     * @throws Exception
     */
    @Test
    public void ofdSignByParamKeyWordAll() throws Exception {
        //签章规则编号
        String serverSealNum = "GM_95d05b6968e30ca5f2d0d30c165fb01600c362e4";
        //pdf字节数组
        byte[] ofdFileByte = ClientUtil.readFileToByteArray(new File(testOfdFilePath));
        KeywordBean bean = new KeywordBean();
        bean.setPageNo(0); // 签章页码
        bean.setKeyword("的");
        bean.setSearchOrder("1");
        bean.setKeywordNum("0");
        bean.setMoveSize(0); // 橫向偏移量
        bean.setHeightMoveSize(0); // 纵向偏移量
        // 注意若为椭圆章，则印章以较长边等比例缩放
        bean.setSealWidth(0);// 印章宽度（若 宽度<=0 使用印章印章编号中记录的宽度 ）
        bean.setSealHeight(0); // 印章高度 （若 高度<=0 使用印章印章编号中记录的高度 ）
        String taskId = UUID.randomUUID().toString();
        ChannelMessage message = anySignClientTool.ofdSignByParamKeyWords(taskId, ofdFileByte, serverSealNum, bean);
        System.out.println(message.getStatusCode());
        System.out.println(message.getStatusInfo());
        if (message.getStatusCode().equals("200")) {
            ClientUtil.writeByteArrayToFile (new File(testSignedOfdFilePath), message.getBody()); // 签章后的OFD文件字节数组
        }
    }

    /**
     * OFD批量签章
     * @throws Exception
     */
    @Test
    public void ofdSignByParamBatchBoth() throws Exception {
        List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
        //pdf字节数组
        byte[] ofdFileByte = ClientUtil.readFileToByteArray(new File(testOfdFilePath));
        {
            //签章规则编号
            String realRuleNum = "7B38600AEF6111E8";
            ClientSignMessage clientSignMessage = new ClientSignMessage();
            //pdf字节数组
            List<SignRuleBean> signRuleBeanList = new ArrayList<SignRuleBean>();
            SignRuleBean signRuleBean1 = new SignRuleBean();
            //服务器规则编号
            signRuleBean1.setRuleNum(realRuleNum);
            signRuleBeanList.add(signRuleBean1);
            SignRuleBean signRuleBean2 = new SignRuleBean();
            //服务器规则编号
            signRuleBean2.setRuleNum(realRuleNum);
            signRuleBeanList.add(signRuleBean2);
            clientSignMessage.setSealTypeEnum(SealTypeEnum.GM);
            clientSignMessage.setFileBty(ofdFileByte);//签章文件
            clientSignMessage.setFileUniqueId(UUID.randomUUID().toString());
            clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
            messages.add(clientSignMessage);
        }
        //签章规则编号
        String realSealNum = "GM_95d05b6968e30ca5f2d0d30c165fb01600c362e4";
        {
            ClientSignMessage clientSignMessage = new ClientSignMessage();
            //pdf字节数组
            List<SignRuleBean> signRuleBeanList = new ArrayList<SignRuleBean>();
            SignRuleBean signRuleBean1 = new SignRuleBean();
            //关键字对象
            KeywordBean bean1 = new KeywordBean();
            bean1.setServerSealNum(realSealNum);
            bean1.setPageNo(0); // 签章页码（0 全部页， 1 第一页 ……）
            bean1.setKeyword("的"); // 关键字
            bean1.setSearchOrder("1"); // 正序搜索倒序搜索（1 正序 2倒序）
            bean1.setKeywordNum("0"); // 第几个关键字（当前pageNo下 ：0全部， 1 第一个……）
            bean1.setMoveSize(0); // 橫向偏移量
            bean1.setHeightMoveSize(0); // 纵向偏移量
            // 注意若为椭圆章，则印章以较长边等比例缩放
            bean1.setSealWidth(0); // 印章宽度（若 宽度<=0 使用印章印章编号中记录的宽度 ）
            bean1.setSealHeight(0); // 印章高度 （若 高度<=0 使用印章印章编号中记录的高度 ）
            signRuleBean1.setKeywordBean(bean1);
            signRuleBeanList.add(signRuleBean1);
            SignRuleBean signRuleBean2 = new SignRuleBean();
            //关键字对象
            KeywordBean bean2 = new KeywordBean();
            bean2.setServerSealNum(realSealNum);
            bean2.setPageNo(0); // 签章页码（0 全部页， 1 第一页 ……）
            bean2.setKeyword("的"); // 关键字
            bean2.setSearchOrder("1"); // 正序搜索倒序搜索（1 正序 2倒序）
            bean2.setKeywordNum("0"); // 第几个关键字（当前pageNo下 ：0全部， 1 第一个……）
            bean2.setMoveSize(0); // 橫向偏移量
            bean2.setHeightMoveSize(0); // 纵向偏移量
            // 注意若为椭圆章，则印章以较长边等比例缩放
            bean2.setSealWidth(0); // 印章宽度（若 宽度<=0 使用印章印章编号中记录的宽度 ）
            bean2.setSealHeight(0); // 印章高度 （若 高度<=0 使用印章印章编号中记录的高度 ）
            signRuleBean2.setKeywordBean(bean2);
            signRuleBeanList.add(signRuleBean2);
            clientSignMessage.setSealTypeEnum(SealTypeEnum.GM);
            clientSignMessage.setFileBty(ofdFileByte);//签章文件
            clientSignMessage.setFileUniqueId(UUID.randomUUID().toString());
            clientSignMessage.setSignRuleBeanList(signRuleBeanList);
            messages.add(clientSignMessage);

        }

        {
            ClientSignMessage clientSignMessage = new ClientSignMessage();
            //pdf字节数组
            List<SignRuleBean> signRuleBeanList = new ArrayList<SignRuleBean>();
            SignRuleBean signRuleBean1 = new SignRuleBean();
            //服务器印章编号
            //坐标定位对象
            RectangleBean rectangleBean1 = new RectangleBean();
            rectangleBean1.setServerSealNum(realSealNum);
            rectangleBean1.setPageNo(1); // 签章页码（0 全部页， 1 第一页 ……）
            rectangleBean1.setLeft(40); // 印章距离OFD左边界
            rectangleBean1.setTop(40); //  印章距离OFD上边界
            // 注意若为椭圆章，则印章以较长边等比例缩放
            rectangleBean1.setSealWidth(0);// 印章宽度（若 宽度<=0 使用印章印章编号中记录的宽度 ）
            rectangleBean1.setSealHeight(0); // 印章高度 （若 高度<=0 使用印章印章编号中记录的高度 ）
            signRuleBean1.setRectangleBean(rectangleBean1);
            signRuleBeanList.add(signRuleBean1);
            SignRuleBean signRuleBean2 = new SignRuleBean();
//        //服务器印章编号
            //坐标定位对象
            RectangleBean rectangleBean2 = new RectangleBean();
            rectangleBean2.setServerSealNum(realSealNum);
            rectangleBean2.setPageNo(1); // 签章页码（0 全部页， 1 第一页 ……）
            rectangleBean2.setLeft(40); // 印章距离OFD左边界
            rectangleBean2.setTop(40); //  印章距离OFD上边界
            // 注意若为椭圆章，则印章以较长边等比例缩放
            rectangleBean2.setSealWidth(0);// 印章宽度（若 宽度<=0 使用印章印章编号中记录的宽度 ）
            rectangleBean2.setSealHeight(0); // 印章高度 （若 高度<=0 使用印章印章编号中记录的高度 ）
            signRuleBean2.setRectangleBean(rectangleBean2);
            signRuleBeanList.add(signRuleBean2);
            clientSignMessage.setSealTypeEnum(SealTypeEnum.GM);
            clientSignMessage.setFileBty(ofdFileByte);//签章文件
            clientSignMessage.setFileUniqueId(UUID.randomUUID().toString());
            clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
            messages.add(clientSignMessage);
        }

        String taskId = UUID.randomUUID().toString();
        ChannelMessage message = anySignClientTool.ofdBatchSignByParam(taskId, messages);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        // 成功，获取签章文件
        if ("200".equals(message.getStatusCode())) {
            List<ClientSignBean> signBeanList = message.getClientSignList();
            System.out.println("签章结果列表：");
            for (int i = 0; i < signBeanList.size(); i++) {
                System.out.println("签章流水:" + signBeanList.get(i).getSignUniqueId() + "##状态码：" + signBeanList.get(i).getStatusCode() + "##状态信息：" + signBeanList.get(i).getStatusInfo());
                if ("200".equals(signBeanList.get(i).getStatusCode())) {
                    String name = resourcesPath + "out" + File.separator +"ofdSignByParamBatchBoth" + i + ".ofd";
                    System.out.println(name);
                    ClientUtil.writeByteArrayToFile(new File(name), signBeanList.get(i).getPdfBty()); // 签章后的OFD文件字节数组
                }
            }
        }
    }


    /**
     * OFD验签
     * @throws Exception
     */
    @Test
    public void ofdSignVerify() throws Exception {
        //pdf字节数组
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(""));
        String taskId = UUID.randomUUID().toString();
        ChannelMessage message = anySignClientTool.ofdVerify(taskId, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        } else {
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        }
    }


    @Test
    public void testOfdSign() throws Exception { //OFD签章测试
        System.out.println("*******************12");
        byte[] ofdBty = ClientUtil.readFileToByteArray(new File(testOfdFilePath));//pfd字节数组
        String ruleNum = "57C0A47CD7623A3F";//签章规则编号
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.ofdSign(ruleNum, ofdBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedOfdFilePath), message.getBody());
        }
    }

    @Test
    public void testOfdVerifyForNoOrgCode() throws Exception {//OFD验签测试
        System.out.println("******************33");
        byte[] ofdBty = ClientUtil.readFileToByteArray(new File(testSignedOfdFilePath));//ofd字节数组
        ChannelMessage message = anySignClientTool.ofdVerify(ofdBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        } else {
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        }
    }

    @Test
    public void testOfdSignList() throws Exception { //OFD多规则签章测试
        System.out.println("*******************12");
        byte[] ofdBty = ClientUtil.readFileToByteArray(new File(testOfdFilePath));//ofd字节数组
        List<String> ruleNumList = new ArrayList<String>();//签章规则编号列表
        String ruleNum;
        ruleNum = "BEC8BC06E70441A";
        ruleNumList.add(ruleNum);

        ruleNum = "57C0A47CD7623A3F";
        ruleNumList.add(ruleNum);

        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.ofdSign(ruleNumList, ofdBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedOfdFilePath), message.getBody());
        }
    }

    @Test
    public void testGenBarcodeAndOFD() throws Exception {//产生条形码+ofd
        byte[] ofdBty = ClientUtil.readFileToByteArray(new File(testOfdFilePath));
        String ruleNum = "9E15A6E6D004FF5";
        String contents = "123456789";
        ChannelMessage message = anySignClientTool.genBarcodeAndOFD(ruleNum, contents, ofdBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedOfdFilePath), message.getBody());
        }
    }

    @Test
    public void testOFDSignByParam() throws Exception {//客户端传参OFD签章
        byte[] inputFile = ClientUtil.readFileToByteArray(new File(testOfdFilePath));
        RectangleBean bean = new RectangleBean();
        bean.setPageNo(1); // 签章页码
        bean.setLeft(20); // 坐标
        bean.setBottom(74f); // 坐标
        String serverSealNum = "1E69DBB775F2D6A8"; // 服务器印章编号   148：396A964840EAA984
        String certSn = "53C130E7BF352E1F"; // 签名证书序列号 148: RSA:3F89508445039559  SM2:8521520202
        boolean isPrint = true; // 只支持打印只能设置成true
        ChannelMessage message = anySignClientTool.ofdSignByParam(inputFile, serverSealNum, certSn, isPrint, bean);
        System.out.println(message.getStatusCode());
        System.out.println(message.getStatusInfo());
        if (message.getStatusCode().equals("200")) { // 200表示成功，其他出现错误
            FileUtils.writeByteArrayToFile(new File(testSignedOfdFilePath), message.getBody()); // 签章后的OFD文件字节数组
        }
    }
    
    /**
     * PDF 获取签章规则
     * v2.0.5 新增
     *
     * @throws Exception
     */
    @Test
    public void testGtSignSealRule() throws Exception {
        ChannelMessage message = anySignClientTool.getSignSealRule("53582C51C506201E");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        System.out.println(new String(message.getHead()));
        System.out.println(new String(message.getBody()));
    }

    /**
     * PDF Hash 签名demo
     * v2.0.5 新增
     *
     * @throws Exception
     */
    @Test
    public void testHashSign() throws Exception {
        String hashBase64 = "5UrvxYBFaIB4CTnCEDLYoTSpvKxi4kz5erCsVVGsTBc=";
        ChannelMessage message = anySignClientTool.getHashSign("53582C51C506201E", hashBase64);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        System.out.println(new String(message.getHead()));
        System.out.println(new String(message.getBody()));
    }
    
    
    
}

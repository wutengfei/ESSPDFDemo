package cn.org.bjca;

import cn.org.bjca.anysign.terminal.model.TimeTag;
import cn.org.bjca.common.model.FontStyle;
import cn.org.bjca.seal.esspdf.client.message.*;
import cn.org.bjca.seal.esspdf.client.tools.AnySignClientTool;
import cn.org.bjca.seal.esspdf.client.utils.ClientUtil;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************
 * <pre>PDF签章服务器客户端工具测试类</pre>
 * @文件名称:  anySignClientToolTest.java
 * @包   路   径：  cn.org.bjca.seal.esspdf.client.test
 * @版权所有：北京数字认证股份有限公司 (C) 2013
 *
 * @类描述:  
 * @版本: V2.0
 * @创建人： wangwenc
 * @创建时间：2013-5-18 下午1:34:10
 ***************************************************************************/
public class ESSPDFClientPortalToolTest{
	
	AnySignClientTool anySignClientTool = null;

	private String resourcesPath;
    String testPdfFilePath;
    String testSignedPdfFilePath;
    String testImageFilePath;
    String outPdfsFilePath="D://esspdf/";

	@Before
	public void init() throws Exception {
		String ip = "223.70.139.221";
		int port = 8888;
		anySignClientTool = new AnySignClientTool(ip, port);
        //客户端软负载
//      anySignClientTool = new AnySignClientTool("127.0.0.1:8888","192.168.126.148:8888");
		//连接服务器超时时间，单位是毫秒，默认为5秒，当前为50秒
		anySignClientTool.setTimeout(50000);//连接超时时间设置
		anySignClientTool.setRespTimeout(50000);//响应超时时间
		resourcesPath = URLDecoder.decode(this.getClass().getClassLoader().getResource("").getPath(), "UTF-8");
		testPdfFilePath = resourcesPath + "pdf" + File.separator + "test.pdf";
        testSignedPdfFilePath = resourcesPath + "pdf" + File.separator + "signedPDF.pdf";
        testImageFilePath = resourcesPath + "image" + File.separator + "test.gif";
	}

	//3.1.15.1.1.	客户端传所有签章参数基于坐标定位签章
	@Test
	public void testFileSignByParam1 () throws Exception {		
        List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
          //pdf字节数组
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean1=new SignRuleBean();
		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略
		String certSN1 = "53C130E7BF352E1F";//签名策略编号
		String serverSealNum1 = "1E69DBB775F2D6A8";//服务器印章编号
		RectangleBean rectangleBean1= new RectangleBean();
		rectangleBean1.setTssCertSN(tssCertSN1);
		rectangleBean1.setServerSealNum(serverSealNum1);
		rectangleBean1.setCertSN(certSN1);
		rectangleBean1.setPageNo(1);
		rectangleBean1.setLeft(400f);
		rectangleBean1.setTop(100f);
		rectangleBean1.setRight(300f);
		rectangleBean1.setBottom(200f);
		signRuleBean1.setRectangleBean(rectangleBean1);//坐标定位对象
		signRuleBeanList.add(signRuleBean1);
		
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略
		String certSN2 = "53C130E7BF352E1F";//签名策略编号
		String serverSealNum2 = "1E69DBB775F2D6A8";//服务器印章编号
		RectangleBean rectangleBean2 = new RectangleBean();
		rectangleBean2.setTssCertSN(tssCertSN2);
		rectangleBean2.setServerSealNum(serverSealNum2);
		rectangleBean2.setCertSN(certSN2);
		rectangleBean2.setPageNo(1);
		rectangleBean2.setLeft(100f);
		rectangleBean2.setTop(100f);
		rectangleBean2.setRight(100f);
		rectangleBean2.setBottom(100f);
		signRuleBean2.setRectangleBean(rectangleBean2);//坐标定位对象
		signRuleBeanList.add(signRuleBean2);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
	     clientSignMessage.setFileUniqueId("1111");
	     clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
	     clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
	     messages.add(clientSignMessage);	  	
        ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
	     System.out.println("状态码：" + message.getStatusCode());
	     System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	//3.1.15.1.1.	客户端传所有签章参数基于坐标定位签章,使用服务器印章编号绑定的签名策略
	@Test
	public void testFileSignByParam1ToServerSealNum () throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		//pdf字节数组
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean1=new SignRuleBean();
		String tssCertSN1 ="472A583F045CE94A";//时间戳签名策略
		String serverSealNum1 = "test3";//绑定签名策略的服务器印章编号
		RectangleBean rectangleBean1= new RectangleBean();
		rectangleBean1.setTssCertSN(tssCertSN1);
		rectangleBean1.setServerSealNum(serverSealNum1);
		rectangleBean1.setPageNo(1);
		rectangleBean1.setLeft(400f);
		rectangleBean1.setTop(100f);
		rectangleBean1.setRight(300f);
		rectangleBean1.setBottom(200f);
		signRuleBean1.setRectangleBean(rectangleBean1);//坐标定位对象
		//添加时间标记
		TimeTag timeTag = new TimeTag();
		timeTag.setPos("2");//1：在签名图片上方，2：在签名图片下方，3：在签名图片的右方，4：使用偏移（XOffset（左右偏移），YOffset（上下偏移），unit（单位：pt））
		timeTag.setFormat("yyyy年MM月dd日");//时间格式可配置
		timeTag.setFontStyles(FontStyle.SIMSUN);
		timeTag.setFontSize(12);//字体大小。可配置，默认为12
		timeTag.setFontColor("black");//颜色设置支持为两种方式：1、标准英文代码：red，blue，black等；2、颜色代码：#FF0000等
		timeTag.setBold(true);
		signRuleBean1.setTimeTag(timeTag);

		signRuleBeanList.add(signRuleBean1);

		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略
		//String certSN2 = "53C130E7BF352E1F";//签名策略编号
		String serverSealNum2 = "test1";//服务器印章编号
		RectangleBean rectangleBean2 = new RectangleBean();
		rectangleBean2.setTssCertSN(tssCertSN2);
		rectangleBean2.setServerSealNum(serverSealNum2);
		//	rectangleBean2.setCertSN(certSN2);
		rectangleBean2.setPageNo(1);
		rectangleBean2.setLeft(100f);
		rectangleBean2.setTop(100f);
		rectangleBean2.setRight(100f);
		rectangleBean2.setBottom(100f);
		signRuleBean2.setRectangleBean(rectangleBean2);//坐标定位对象
		signRuleBeanList.add(signRuleBean2);

		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (int i = 0; i < signBeanList.size(); i++) {
				System.out.println("签章流水:"+signBeanList.get(i).getSignUniqueId()+"##状态码："+signBeanList.get(i).getStatusCode()+"##状态信息："+signBeanList.get(i).getStatusInfo());
				System.out.println(outPdfsFilePath);
				if("200".equals(signBeanList.get(i).getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File(outPdfsFilePath + "testFileSignByParam1" + i + ".pdf"), signBeanList.get(i).getPdfBty());
				}
			}
		}
	}
	//3.1.15.1.2.	客户端传所有签章参数基于关键字定位签章
	//客户端传多个关键字签章（传签章图片base64编码）
    @Test
    public void testFileSignByParam2() throws Exception {		 
        List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
        //pdf字节数组
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
		//image字节数组
        byte[] imageBty1 = ClientUtil.readFileToByteArray(new File(testImageFilePath));		
        List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN ="52178EDD282F07D5";//时间戳签名策略
		String certSN = "53C130E7BF352E1F";//签名策略编号
		String sealImg1 =Base64.encode(imageBty1);
		KeywordBean bean = new KeywordBean();
		bean.setTssCertSN(tssCertSN);
		bean.setCertSN(certSN);
		bean.setSealImg(sealImg1);
		bean.setKeyword("东吴");
		bean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		bean.setKeywordNum("3");
		bean.setMoveType("1");
		bean.setIsPrint("false");
		bean.setMoveSize(0);
		bean.setHeightMoveSize(0);
		signRuleBean.setKeywordBean(bean);//坐标定位对象
		signRuleBeanList.add(signRuleBean);
		//image字节数组
		byte[] imageBty2 = ClientUtil.readFileToByteArray(new File(testImageFilePath));
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略
		String certSN2 = "53C130E7BF352E1F";//签名策略编号
		String sealImg2 = Base64.encode(imageBty2);
		KeywordBean bean2 = new KeywordBean();
		bean2.setTssCertSN(tssCertSN2);
		bean2.setCertSN(certSN2);
		bean2.setSealImg(sealImg2);
		bean2.setKeyword("东吴");
		bean2.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		bean2.setKeywordNum("3");
		bean2.setMoveType("1");
		bean2.setIsPrint("false");
		bean2.setMoveSize(0);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(bean2);//关键字定位对象
		signRuleBeanList.add(signRuleBean2);
	     clientSignMessage.setFileBty(fileBty);//签章文件
	     clientSignMessage.setFileUniqueId("1111");
	     clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
	     clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
	     messages.add(clientSignMessage);	
         ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
	     System.out.println("状态码：" + message.getStatusCode());
	     System.out.println("状态信息："+message.getStatusInfo());
	if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
		List<ClientSignBean> signBeanList = message.getClientSignList();
		System.out.println("签章结果列表：");
		for (ClientSignBean beanObj : signBeanList) {
			System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
			if("200".equals(beanObj.getStatusCode())){
				ClientUtil.writeByteArrayToFile(new File("C:/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
			}
		}
	}
	}
	//3.1.15.1.2.	客户端传所有签章参数基于关键字定位签章,使用服务器印章编号绑定的签名策略
    @Test
    public void testFileSignByParam2ToServerSealNum() throws Exception {		 
        List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
        //pdf字节数组
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
		//image字节数组
        byte[] imageBty1 = ClientUtil.readFileToByteArray(new File(testImageFilePath));		
        List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN ="52178EDD282F07D5";//时间戳签名策略
		String serverSealNum="123455";//绑定签名策略的服务器印章编号
		KeywordBean bean = new KeywordBean();
		bean.setTssCertSN(tssCertSN);
		bean.setServerSealNum(serverSealNum);
		bean.setKeyword("东吴");
		bean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		bean.setKeywordNum("3");
		bean.setMoveType("1");
		bean.setIsPrint("false");
		bean.setMoveSize(0);
		bean.setHeightMoveSize(0);
		signRuleBean.setKeywordBean(bean);//坐标定位对象
		signRuleBeanList.add(signRuleBean);
		//image字节数组
		byte[] imageBty2 = ClientUtil.readFileToByteArray(new File(testImageFilePath));
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略
		String serverSealNum2="test1";//绑定签名策略的服务器印章编号
		KeywordBean bean2 = new KeywordBean();
		bean2.setTssCertSN(tssCertSN2);
		bean2.setServerSealNum(serverSealNum2);
		bean2.setKeyword("合同编号");
		bean2.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		bean2.setKeywordNum("3");
		bean2.setMoveType("1");
		bean2.setIsPrint("false");
		bean2.setMoveSize(0);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(bean2);//关键字定位对象
		signRuleBeanList.add(signRuleBean2);
	     clientSignMessage.setFileBty(fileBty);//签章文件
	     clientSignMessage.setFileUniqueId("1111");
	     clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
	     clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
	     messages.add(clientSignMessage);	
         ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
	     System.out.println("状态码：" + message.getStatusCode());
	     System.out.println("状态信息："+message.getStatusInfo());
	if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
		List<ClientSignBean> signBeanList = message.getClientSignList();
		System.out.println("签章结果列表：");
		for (ClientSignBean beanObj : signBeanList) {
			System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
			if("200".equals(beanObj.getStatusCode())){
				ClientUtil.writeByteArrayToFile(new File("C:/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
			}
		}
	}
	}


    //3.1.15.1.3.	客户端传所有签章参数基于表单域定位签章
    @Test
   	public void testFileSignByFieldBeanParam3() throws Exception {	
            List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
            //pdf字节数组
   		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
   		ClientSignMessage clientSignMessage = new ClientSignMessage();
   		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
   		SignRuleBean signRuleBean1=new SignRuleBean();
   		FieldBean bean1 = new FieldBean();
   		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略
   		String certSN1 = "53C130E7BF352E1F";//签名策略编号
   		String serverSealNum1 = "1E69DBB775F2D6A8";//服务器印章编号
   		bean1.setCertSN(certSN1);
   		bean1.setTssCertSN(tssCertSN1);
   		bean1.setServerSealNum(serverSealNum1);
   		bean1.setKeyword("requestdate");//表单域名称（必填）
   		bean1.setIsPrint("true");
   		bean1.setMoveType("1");
   		bean1.setMoveSize(200);
   		bean1.setHeightMoveSize(0);
   		signRuleBean1.setFieldBean(bean1);//表单域定位对象
   		signRuleBeanList.add(signRuleBean1);
   		
   		clientSignMessage.setFileBty(fileBty);//签章文件
   	     clientSignMessage.setFileUniqueId("1111");
   	     clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
   	     clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
   	     messages.add(clientSignMessage);	
        ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
   	     System.out.println("状态码：" + message.getStatusCode());
   	     System.out.println("状态信息："+message.getStatusInfo());
   	if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
   		List<ClientSignBean> signBeanList = message.getClientSignList();
   		System.out.println("签章结果列表：");
   		for (ClientSignBean beanObj : signBeanList) {
   			System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
   			if("200".equals(beanObj.getStatusCode())){
   				ClientUtil.writeByteArrayToFile(new File("C:/Users/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
   			}
   		}
   	}
   	}
	
		//3.1.15.1.3.	客户端传所有签章参数基于表单域定位签章,使用服务器印章编号绑定的签名策略
	@Test
	public void testFileSignByFieldBeanParam3ToServerSealNum() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		//pdf字节数组
		byte[] fileBty = ClientUtil.readFileToByteArray(new File("C:\\Users\\lomoida\\Desktop\\签名域-空.pdf"));
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean1=new SignRuleBean();
		FieldBean bean1 = new FieldBean();
		String tssCertSN1 ="472A583F045CE94A";//时间戳签名策略
		String serverSealNum1 = "test1";//服务器印章编号
		bean1.setTssCertSN(tssCertSN1);
		bean1.setServerSealNum(serverSealNum1);
		bean1.setKeyword("H_7a68616e6773616e232337363636363636363636232332");//表单域名称（必填）
		bean1.setIsPrint("true");
		bean1.setMoveType("1");
		bean1.setMoveSize(200);
		bean1.setHeightMoveSize(0);
		signRuleBean1.setFieldBean(bean1);//表单域定位对象
		signRuleBeanList.add(signRuleBean1);

		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("Field");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}

    //3.1.15.1.4.	客户端传参基于签章规则及坐标定位签章
    @Test
    public void testClientFileBatchSignByClientRectangleBean4() throws Exception {
    	List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
    	ClientSignMessage clientSignMessage = new ClientSignMessage();
        //pdf字节数组
    	byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));	
    	List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
    	SignRuleBean signRuleBean=new SignRuleBean();
    	String ruleNum = "53582C51C506201E";//签章规则编号
    	RectangleBean bean=new RectangleBean();
    	bean.setRuleNum(ruleNum);
    	bean.setPageNo(1);
    	bean.setLeft(200f);
    	bean.setTop(300f);
    	bean.setRight(300f);
    	bean.setBottom(200f);
    	bean.setIsPrint("false");
    	bean.setMoveType("1");//中心点坐标只能1覆盖
    	bean.setMoveSize(300);
    	bean.setHeightMoveSize(0);
    	signRuleBean.setRectangleBean(bean);
    	signRuleBeanList.add(signRuleBean);
    	
    	clientSignMessage.setFileBty(fileBty);//签章文件
    	clientSignMessage.setFileUniqueId("1111文件");
        //签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
    	clientSignMessage.setEntityType(ENTITYType.PDF);	clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
    	messages.add(clientSignMessage);
    	
    	ClientSignMessage clientSignMessage2 = new ClientSignMessage();
         //pdf字节数组
    	byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));	
    	List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
    	SignRuleBean signRuleBean2=new SignRuleBean();
    	String ruleNum2 = "53582C51C506201E";//签章规则编号
    	RectangleBean bean2=new RectangleBean();
    	bean2.setRuleNum(ruleNum2);
    	bean2.setPageNo(1);
    	bean2.setLeft(200f);
    	bean2.setTop(300f);
    	bean2.setRight(300f);
    	bean2.setBottom(200f);
    	bean2.setIsPrint("false");
    	bean2.setMoveType("1");//中心点坐标只能1覆盖
    	bean2.setMoveSize(300);
    	bean2.setHeightMoveSize(0);
    	signRuleBean2.setRectangleBean(bean2);
    	signRuleBeanList2.add(signRuleBean2);
    	clientSignMessage2.setFileBty(fileBty2);//签章文件
       //签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
    	clientSignMessage2.setEntityType(ENTITYType.PDF);	clientSignMessage2.setFileUniqueId("2222文件");
    	clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
    	messages.add(clientSignMessage2);

    	ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
    	System.out.println("状态码：" + message.getStatusCode());
    	System.out.println("状态信息："+message.getStatusInfo());
    	if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
    		List<ClientSignBean> signBeanList = message.getClientSignList();
    		System.out.println("签章结果列表：");
    		for (ClientSignBean beanObj : signBeanList) {
    			System.out.println("签章流水:"+
                      beanObj.getSignUniqueId()+"##状态码："+
                      beanObj.getStatusCode()+"##状态信息："+
                      beanObj.getStatusInfo());
    			if("200".equals(beanObj.getStatusCode())){
    			ClientUtil.writeByteArrayToFile(new  
                   File("D:\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
    			}
    		}
    	}
    }

    //3.1.15.1.5.	客户端传参基于签章规则及关键字定位签章
    @Test
    public void testClientFileBatchSignByClientKeywordBean5() throws Exception {
    	List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
    	ClientSignMessage clientSignMessage = new ClientSignMessage();
        //pdf字节数组
    	byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));	
    	List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
    	SignRuleBean signRuleBean=new SignRuleBean();
    	String ruleNum = "53582C51C506201E";//签章规则编号
    	KeywordBean keywordBean = new KeywordBean();
    	keywordBean.setRuleNum(ruleNum);
    	keywordBean.setKeyword("东吴");
    	keywordBean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
    	keywordBean.setKeywordNum("3");
    	keywordBean.setMoveType("2");
    	keywordBean.setIsPrint("true");
    	keywordBean.setMoveSize(0);
    	keywordBean.setHeightMoveSize(0);
    	signRuleBean.setKeywordBean(keywordBean);//关键字定位对象
    	signRuleBeanList.add(signRuleBean);
    	clientSignMessage.setFileBty(fileBty);//签章文件
    	clientSignMessage.setFileUniqueId("11111文件");
        //签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
    	clientSignMessage.setEntityType(ENTITYType.PDF);	clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
    	messages.add(clientSignMessage);
    	ClientSignMessage clientSignMessage2 = new ClientSignMessage();
         //pdf字节数组
    	byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));	
    	List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
    	SignRuleBean signRuleBean2=new SignRuleBean();
    	String ruleNum2 = "53582C51C506201E";//签章规则编号
    	KeywordBean keywordBean1 = new KeywordBean();
    	keywordBean1.setRuleNum(ruleNum2);
    	keywordBean1.setKeyword("东吴");
    	keywordBean1.setSearchOrder("2"); // 检索顺序0:全部、 1:正序、2:倒序
    	keywordBean1.setKeywordNum("3");
    	keywordBean1.setMoveType("2");
    	keywordBean1.setIsPrint("true");
    	keywordBean1.setMoveSize(0);
    	keywordBean1.setHeightMoveSize(0);
    	signRuleBean2.setKeywordBean(keywordBean1);//关键字定位对象
    	signRuleBeanList2.add(signRuleBean2);
    	signRuleBeanList2.add(signRuleBean2);
    	clientSignMessage2.setFileBty(fileBty2);//签章文件
         //签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
    	clientSignMessage2.setEntityType(ENTITYType.PDF);	clientSignMessage2.setFileUniqueId("2222文件");
    	clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
    	messages.add(clientSignMessage2);
    	ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
    	System.out.println("状态码：" + message.getStatusCode());
    	System.out.println("状态信息："+message.getStatusInfo());
    	if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
    		List<ClientSignBean> signBeanList = message.getClientSignList();
    		System.out.println("签章结果列表：");
    		for (ClientSignBean beanObj : signBeanList) {
    			System.out.println("签章流水:"+beanObj.getSignUniqueId()+
                       "##状态码："+beanObj.getStatusCode()+
                       "##状态信息："+beanObj.getStatusInfo());
    			if("200".equals(beanObj.getStatusCode())){
    				ClientUtil.writeByteArrayToFile(
                  new File("D:\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
    			}
    		}
    	}
    }

    //3.1.15.1.6.	客户端传参基于签章规则及表单域定位签章
    @Test
    public void testFileSignByFieldBeanParamFieldBean6() throws Exception {	
       List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
    //PDF字节数组
       byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
       //image字节数组
       byte[] imageBty = ClientUtil.readFileToByteArray(new File(testImageFilePath));
       ClientSignMessage clientSignMessage = new ClientSignMessage();
       List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
       SignRuleBean signRuleBean1=new SignRuleBean();
       String ruleNum1 = "53582C51C506201E";//签章规则编号
       FieldBean fieldBean = new FieldBean();
       fieldBean.setKeyword("requestdate");//表单域名称（必填）
       fieldBean.setRuleNum(ruleNum1);
       signRuleBean1.setFieldBean(fieldBean);//表单域定位对象
       signRuleBeanList.add(signRuleBean1);
       SignRuleBean signRuleBean2=new SignRuleBean();
       FieldBean fieldBean2 = new FieldBean();
       String tssCertSN ="52178EDD282F07D5";//时间戳签名策略
       String certSN = "53C130E7BF352E1F";//签名策略编号
       String sealImg = Base64.encode(imageBty);
       fieldBean2.setCertSN(certSN);
       fieldBean2.setTssCertSN(tssCertSN);
       fieldBean2.setSealImg(sealImg);
       fieldBean2.setKeyword("requestdate");//表单域名称（必填）
       fieldBean2.setIsPrint("true");
       fieldBean2.setMoveType("1");
       fieldBean2.setMoveSize(200);
       fieldBean2.setHeightMoveSize(0);
       signRuleBean2.setFieldBean(fieldBean2);//表单域定位对象
       signRuleBeanList.add(signRuleBean2);
       clientSignMessage.setFileBty(fileBty);//签章文件
       clientSignMessage.setFileUniqueId("1111");
       clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
       clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
       messages.add(clientSignMessage);	
       ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
       System.out.println("状态码：" + message.getStatusCode());
       System.out.println("状态信息："+message.getStatusInfo());
       if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
    		List<ClientSignBean> signBeanList = message.getClientSignList();
    		System.out.println("签章结果列表：");
    		for (ClientSignBean beanObj : signBeanList) {
    			System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
    			if("200".equals(beanObj.getStatusCode())){
    				ClientUtil.writeByteArrayToFile(new File("C:\\Users\\spirit\\Desktop\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
    			}
    		}
    	  }
    	}

    //3.1.15.1.7.	客户端传入组合规则签章
    @Test
   	public void testFileBatchSignByParam7() throws Exception {
   		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
   		ClientSignMessage clientSignMessage = new ClientSignMessage();
   		//pdf字节数组
   		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
   		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
   		SignRuleBean signRuleBean=new SignRuleBean();
   		String ruleNum ="53582C51C506201E";//签章规则编号 
   		String certSN = "53C130E7BF352E1F";//签名策略编号
   		String serverSealNum = "1E69DBB775F2D6A8";//服务器印章编号
   		RectangleBean bean = new RectangleBean();
   		bean.setRuleNum(ruleNum);
   		bean.setCertSN(certSN);
   		bean.setServerSealNum(serverSealNum);
   		signRuleBean.setRectangleBean(bean);
   		signRuleBeanList.add(signRuleBean);
   		clientSignMessage.setFileUniqueId("test1文件");
   		clientSignMessage.setFileBty(fileBty);//签章文件
   		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型
   		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
   		messages.add(clientSignMessage);
   		
   		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
   		//pdf字节数组
   		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
   		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
   		SignRuleBean signRuleBean2=new SignRuleBean();
   		RectangleBean bean2 = new RectangleBean();
   		String ruleNum2 = "53582C51C506201E";//签章规则编号
   		String certSN2 = "53C130E7BF352E1F";//签名策略编号
   		String serverSealNum2 = "1E69DBB775F2D6A8";//服务器印章编号
   		bean2.setRuleNum(ruleNum2);
   		bean2.setCertSN(certSN2);
   		bean2.setServerSealNum(serverSealNum2);
   		signRuleBean2.setRectangleBean(bean2);
   		signRuleBeanList2.add(signRuleBean2);
   		clientSignMessage2.setFileUniqueId("test2文件");
   		clientSignMessage2.setFileBty(fileBty2);//签章文件
   		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型
   		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
   		messages.add(clientSignMessage2);
   		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
   		System.out.println("状态码：" + message.getStatusCode());
   		System.out.println("状态信息："+message.getStatusInfo());
   		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
   			List<ClientSignBean> signBeanList = message.getClientSignList();
   			System.out.println("签章结果列表：");
   			for (ClientSignBean beanObj : signBeanList) {
   				System.out.println("签章流水:"+
                       beanObj.getSignUniqueId()+"##状态码："+
                       beanObj.getStatusCode()+"##状态信息："+
                       beanObj.getStatusInfo());
   				if("200".equals(beanObj.getStatusCode())){
   				ClientUtil.writeByteArrayToFile(
                 new File("D:\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
   				}
   			}
   		}
   	}
    
	@Test
	public void testClientFileBatchSignByClientRectangleBean() throws Exception {//客户端批量签章（支持全坐标，传关键字）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "53582C51C506201E";//签章规则编号
		RectangleBean bean=new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "35FDC50134878E0";//签章规则编号
		RectangleBean bean2=new RectangleBean();
		bean2.setRuleNum(ruleNum2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		bean2.setIsPrint("false");
		bean2.setMoveType("1");//中心点坐标只能1覆盖
		bean2.setMoveSize(300);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("C:\\Users\\lyr\\Desktop\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	@Test
	public void testClientFileBatchSignByClient12() throws Exception {//客户端批量签章（支持全坐标，传关键字）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "5E9217F50C3E491D";//签章规则编号
		RectangleBean bean=new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File("D:\\测试文件\\PDF测试\\demo.html"));//html字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "5E9217F50C3E491D";//签章规则编号
		RectangleBean bean2=new RectangleBean();
		bean2.setRuleNum(ruleNum2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		bean2.setIsPrint("false");
		bean2.setMoveType("1");//中心点坐标只能1覆盖
		bean2.setMoveSize(300);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.HTML);//签章文件类型
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	@Test
	public void testClientFileBatchSignByClient13() throws Exception {//客户端批量签章（支持全坐标，传关键字）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File("D:\\测试文件\\PDF测试\\test.doc"));//doc字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "5E9217F50C3E491D";//签章规则编号
		RectangleBean bean=new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.DOC);//签章文件类型
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File("D:\\测试文件\\PDF测试\\test.doc"));//doc字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "5E9217F50C3E491D";//签章规则编号
		RectangleBean bean2=new RectangleBean();
		bean2.setRuleNum(ruleNum2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		bean2.setIsPrint("false");
		bean2.setMoveType("1");//中心点坐标只能1覆盖
		bean2.setMoveSize(300);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.DOC);//签章文件类型
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	@Test
	public void testClientFileBatchSignByClient14() throws Exception {//客户端批量签章（支持全坐标，传关键字）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File("D:\\测试文件\\PDF测试\\test1.docx"));//docx字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "5E9217F50C3E491D";//签章规则编号
		RectangleBean bean=new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.DOCX);//签章文件类型
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File("D:\\测试文件\\PDF测试\\test1.docx"));//docx字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "5E9217F50C3E491D";//签章规则编号
		RectangleBean bean2=new RectangleBean();
		bean2.setRuleNum(ruleNum2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		bean2.setIsPrint("false");
		bean2.setMoveType("1");//中心点坐标只能1覆盖
		bean2.setMoveSize(300);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.DOCX);//签章文件类型
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	@Test
	public void testClientFileBatchSignByClient21() throws Exception {//客户端批量签章（支持全坐标，传服务器印章编号）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN1 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum="123455";//服务器印章编号
		RectangleBean bean=new RectangleBean();
		bean.setTssCertSN(tssCertSN1);
		bean.setServerSealNum(serverSealNum);
		bean.setCertSN(certSN1);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum2="123455";//服务器印章编号
		RectangleBean bean2=new RectangleBean();
		bean2.setTssCertSN(tssCertSN2);
		bean2.setServerSealNum(serverSealNum2);
		bean.setCertSN(certSN2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		bean2.setIsPrint("false");
		bean2.setMoveType("1");//中心点坐标只能1覆盖
		bean2.setMoveSize(300);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	@Test
	public void testClientFileBatchSignByClient31() throws Exception {//客户端批量签章（支持全坐标，传签章图片base64编码）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN1 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String sealImg1 = "R0lGODlhgAAnAOMAAP8CAv8kZv9mJf9mZv86kP9mrP+QOv+QZv+2Zv+iqP+o5//opf/b2//b////2////yH5BAEAAA8ALAAAAACAACcAAAT+8MlJq70467qA98UmjmRpnqjVfUCYok4yCALrCYPy7vxlsITeJZaQOA42QILRkKwAQaE09fu4pk5PomYLNClPqHCR0GFHCJb5XE2qJ2HPGsYFeM+b9GcujRcYTwFyDnVvKXFyeBl6HwF8O3FmSG5dAwVfJgyFQIoYbYY8cQGElB8DOS+ISY94R6VRoTY6qnZLY6UArFJMrqUCRT1hsJ+OeLQBnRKbSZdYdWufVme9LFeKDriwUyvIWR+b2lLYe8lGpbo8MmbLq9MsmOWMNjTh27jSWGHoivKgbLgBgAU7wM5asgXsQHTqx6Jbj0m49u1gQDEhgF/Jxrmph4KhG4P+fQBKnKIpyTKQee6JubasWLkKCRqpRKmhjgBBDV8+EYjhiCUGPRx4vOewRJU/heC9fPCET0xOKZ7eCxKDhgeOItiNxPPsAQOIUluc0JgkQYM2WF9EK3qwzISEIeSlxcDQ5YM2NFOQLZX3BMI3e/f89YemmlK8uyDOlLKsgaovVT2wHeGzmQXEkFRS2qqhjQIuOqJ5UJrMcyzNSeaKeBKkSpRJBZouvUuOh+haTCYw2CKZR1fZDxwAZYqvnGkepGqRVtRV450KaF8y4jzbxHRvkhVgipSM0fLqpwFsR51IETfwzEdTSK6SuokVqtGj6IBVxjIcZsvV6Cu/v3QB7vkSJ+CABBZo4IEIJqjgggw2aEIEADs=";
		RectangleBean bean1=new RectangleBean();
		bean1.setTssCertSN(tssCertSN1);
		bean1.setCertSN(certSN1);
		bean1.setSealImg(sealImg1);
		bean1.setPageNo(1);
		bean1.setLeft(200f);
		bean1.setTop(300f);
		bean1.setRight(300f);
		bean1.setBottom(200f);
		bean1.setIsPrint("false");
		bean1.setMoveType("1");//中心点坐标只能1覆盖
		bean1.setMoveSize(300);
		bean1.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean1);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String sealImg2 = "R0lGODlhgAAnAOMAAP8CAv8kZv9mJf9mZv86kP9mrP+QOv+QZv+2Zv+iqP+o5//opf/b2//b////2////yH5BAEAAA8ALAAAAACAACcAAAT+8MlJq70467qA98UmjmRpnqjVfUCYok4yCALrCYPy7vxlsITeJZaQOA42QILRkKwAQaE09fu4pk5PomYLNClPqHCR0GFHCJb5XE2qJ2HPGsYFeM+b9GcujRcYTwFyDnVvKXFyeBl6HwF8O3FmSG5dAwVfJgyFQIoYbYY8cQGElB8DOS+ISY94R6VRoTY6qnZLY6UArFJMrqUCRT1hsJ+OeLQBnRKbSZdYdWufVme9LFeKDriwUyvIWR+b2lLYe8lGpbo8MmbLq9MsmOWMNjTh27jSWGHoivKgbLgBgAU7wM5asgXsQHTqx6Jbj0m49u1gQDEhgF/Jxrmph4KhG4P+fQBKnKIpyTKQee6JubasWLkKCRqpRKmhjgBBDV8+EYjhiCUGPRx4vOewRJU/heC9fPCET0xOKZ7eCxKDhgeOItiNxPPsAQOIUluc0JgkQYM2WF9EK3qwzISEIeSlxcDQ5YM2NFOQLZX3BMI3e/f89YemmlK8uyDOlLKsgaovVT2wHeGzmQXEkFRS2qqhjQIuOqJ5UJrMcyzNSeaKeBKkSpRJBZouvUuOh+haTCYw2CKZR1fZDxwAZYqvnGkepGqRVtRV450KaF8y4jzbxHRvkhVgipSM0fLqpwFsR51IETfwzEdTSK6SuokVqtGj6IBVxjIcZsvV6Cu/v3QB7vkSJ+CABBZo4IEIJqjgggw2aEIEADs=";
		RectangleBean bean2=new RectangleBean();
		bean2.setTssCertSN(tssCertSN2);
		bean2.setSealImg(sealImg2);
		bean2.setCertSN(certSN2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		bean2.setIsPrint("false");
		bean2.setMoveType("1");//中心点坐标只能1覆盖
		bean2.setMoveSize(300);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	@Test
	public void testClientFileBatchSignByClient41() throws Exception {//客户端批量签章（支持坐标与关键字交叉,传关键字）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "5E9217F50C3E491D";//签章规则编号
		RectangleBean bean=new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		bean.setIsPrint("true");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "286BFBD60BF86366";//签章规则编号
		KeywordBean keywordBean = new KeywordBean();
		keywordBean.setRuleNum(ruleNum2);
		keywordBean.setKeyword("seal");
		keywordBean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean.setKeywordNum("3");
		keywordBean.setMoveType("2");
		keywordBean.setIsPrint("false");
		keywordBean.setMoveSize(0);
		keywordBean.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(keywordBean);//关键字定位对象
		signRuleBeanList2.add(signRuleBean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	@Test
	public void testClientFileBatchSignByClient51() throws Exception {//客户端批量签章（支持坐标与关键字交叉，传服务器印章编号）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN1 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum1="123455";//服务器印章编号
		RectangleBean bean1=new RectangleBean();
		bean1.setTssCertSN(tssCertSN1);
		bean1.setServerSealNum(serverSealNum1);
		bean1.setCertSN(certSN1);
		bean1.setPageNo(1);
		bean1.setLeft(200f);
		bean1.setTop(300f);
		bean1.setRight(300f);
		bean1.setBottom(200f);
		bean1.setIsPrint("true");
		bean1.setMoveType("1");//中心点坐标只能1覆盖
		bean1.setMoveSize(300);
		bean1.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean1);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum2="123455";//服务器印章编号
		KeywordBean keywordBean = new KeywordBean();
		keywordBean.setTssCertSN(tssCertSN2);
		keywordBean.setServerSealNum(serverSealNum2);
		keywordBean.setCertSN(certSN2);
		keywordBean.setKeyword("seal");
		keywordBean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean.setKeywordNum("3");
		keywordBean.setMoveType("2");
		keywordBean.setIsPrint("false");
		keywordBean.setMoveSize(0);
		keywordBean.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(keywordBean);//关键字定位对象
		signRuleBeanList2.add(signRuleBean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	@Test
	public void testClientFileBatchSignByClient61() throws Exception {//客户端批量签章（支持坐标与关键字交叉，传印章图片base64编码）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN1 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String sealImg1 = "R0lGODlhgAAnAOMAAP8CAv8kZv9mJf9mZv86kP9mrP+QOv+QZv+2Zv+iqP+o5//opf/b2//b////2////yH5BAEAAA8ALAAAAACAACcAAAT+8MlJq70467qA98UmjmRpnqjVfUCYok4yCALrCYPy7vxlsITeJZaQOA42QILRkKwAQaE09fu4pk5PomYLNClPqHCR0GFHCJb5XE2qJ2HPGsYFeM+b9GcujRcYTwFyDnVvKXFyeBl6HwF8O3FmSG5dAwVfJgyFQIoYbYY8cQGElB8DOS+ISY94R6VRoTY6qnZLY6UArFJMrqUCRT1hsJ+OeLQBnRKbSZdYdWufVme9LFeKDriwUyvIWR+b2lLYe8lGpbo8MmbLq9MsmOWMNjTh27jSWGHoivKgbLgBgAU7wM5asgXsQHTqx6Jbj0m49u1gQDEhgF/Jxrmph4KhG4P+fQBKnKIpyTKQee6JubasWLkKCRqpRKmhjgBBDV8+EYjhiCUGPRx4vOewRJU/heC9fPCET0xOKZ7eCxKDhgeOItiNxPPsAQOIUluc0JgkQYM2WF9EK3qwzISEIeSlxcDQ5YM2NFOQLZX3BMI3e/f89YemmlK8uyDOlLKsgaovVT2wHeGzmQXEkFRS2qqhjQIuOqJ5UJrMcyzNSeaKeBKkSpRJBZouvUuOh+haTCYw2CKZR1fZDxwAZYqvnGkepGqRVtRV450KaF8y4jzbxHRvkhVgipSM0fLqpwFsR51IETfwzEdTSK6SuokVqtGj6IBVxjIcZsvV6Cu/v3QB7vkSJ+CABBZo4IEIJqjgggw2aEIEADs=";
		RectangleBean bean1=new RectangleBean();
		bean1.setTssCertSN(tssCertSN1);
		bean1.setSealImg(sealImg1);
		bean1.setCertSN(certSN1);
		bean1.setPageNo(1);
		bean1.setLeft(200f);
		bean1.setTop(300f);
		bean1.setRight(300f);
		bean1.setBottom(200f);
		bean1.setIsPrint("true");
		bean1.setMoveType("1");//中心点坐标只能1覆盖
		bean1.setMoveSize(300);
		bean1.setHeightMoveSize(0);
		signRuleBean.setRectangleBean(bean1);
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String sealImg2 = "R0lGODlhgAAnAOMAAP8CAv8kZv9mJf9mZv86kP9mrP+QOv+QZv+2Zv+iqP+o5//opf/b2//b////2////yH5BAEAAA8ALAAAAACAACcAAAT+8MlJq70467qA98UmjmRpnqjVfUCYok4yCALrCYPy7vxlsITeJZaQOA42QILRkKwAQaE09fu4pk5PomYLNClPqHCR0GFHCJb5XE2qJ2HPGsYFeM+b9GcujRcYTwFyDnVvKXFyeBl6HwF8O3FmSG5dAwVfJgyFQIoYbYY8cQGElB8DOS+ISY94R6VRoTY6qnZLY6UArFJMrqUCRT1hsJ+OeLQBnRKbSZdYdWufVme9LFeKDriwUyvIWR+b2lLYe8lGpbo8MmbLq9MsmOWMNjTh27jSWGHoivKgbLgBgAU7wM5asgXsQHTqx6Jbj0m49u1gQDEhgF/Jxrmph4KhG4P+fQBKnKIpyTKQee6JubasWLkKCRqpRKmhjgBBDV8+EYjhiCUGPRx4vOewRJU/heC9fPCET0xOKZ7eCxKDhgeOItiNxPPsAQOIUluc0JgkQYM2WF9EK3qwzISEIeSlxcDQ5YM2NFOQLZX3BMI3e/f89YemmlK8uyDOlLKsgaovVT2wHeGzmQXEkFRS2qqhjQIuOqJ5UJrMcyzNSeaKeBKkSpRJBZouvUuOh+haTCYw2CKZR1fZDxwAZYqvnGkepGqRVtRV450KaF8y4jzbxHRvkhVgipSM0fLqpwFsR51IETfwzEdTSK6SuokVqtGj6IBVxjIcZsvV6Cu/v3QB7vkSJ+CABBZo4IEIJqjgggw2aEIEADs=";
		KeywordBean keywordBean = new KeywordBean();
		keywordBean.setTssCertSN(tssCertSN2);
		keywordBean.setSealImg(sealImg2);
		keywordBean.setCertSN(certSN2);
		keywordBean.setKeyword("seal");
		keywordBean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean.setKeywordNum("3");
		keywordBean.setMoveType("2");
		keywordBean.setIsPrint("false");
		keywordBean.setMoveSize(0);
		keywordBean.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(keywordBean);//关键字定位对象
		signRuleBeanList2.add(signRuleBean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	@Test
	public void testClientFileBatchSignByClientKeywordBean() throws Exception {//客户端批量签章（支持全关键字,传签章规则编号）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "53582C51C506201E";//签章规则编号
		KeywordBean keywordBean = new KeywordBean();
		keywordBean.setRuleNum(ruleNum);
		keywordBean.setKeyword("合同编号");
		keywordBean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean.setKeywordNum("3");
		keywordBean.setMoveType("2");
		keywordBean.setIsPrint("true");
		keywordBean.setMoveSize(0);
		keywordBean.setHeightMoveSize(0);
		signRuleBean.setKeywordBean(keywordBean);//关键字定位对象
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("11111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "35FDC50134878E0";//签章规则编号
		KeywordBean keywordBean1 = new KeywordBean();
		keywordBean1.setRuleNum(ruleNum2);
		keywordBean1.setKeyword("释义");
		keywordBean1.setSearchOrder("2"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean1.setKeywordNum("3");
		keywordBean1.setMoveType("2");
		keywordBean1.setIsPrint("true");
		keywordBean1.setMoveSize(0);
		keywordBean1.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(keywordBean1);//关键字定位对象
		signRuleBeanList2.add(signRuleBean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("C:/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	@Test
	public void testClientFileBatchSignByClient81() throws Exception {//客户端批量签章（支持全关键字,传服务器印章编号）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN1 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum1="123455";//服务器印章编号
		KeywordBean keywordBean = new KeywordBean();
		keywordBean.setTssCertSN(tssCertSN1);
		keywordBean.setServerSealNum(serverSealNum1);
		keywordBean.setCertSN(certSN1);
		keywordBean.setKeyword("seal");
		keywordBean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean.setKeywordNum("3");
		keywordBean.setMoveType("2");
		keywordBean.setIsPrint("true");
		keywordBean.setMoveSize(0);
		keywordBean.setHeightMoveSize(0);
		signRuleBean.setKeywordBean(keywordBean);//关键字定位对象
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("11111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum2="123455";//服务器印章编号
		KeywordBean keywordBean1 = new KeywordBean();
		keywordBean1.setTssCertSN(tssCertSN2);
		keywordBean1.setServerSealNum(serverSealNum2);
		keywordBean1.setCertSN(certSN2);
		keywordBean1.setKeyword("seal");
		keywordBean1.setSearchOrder("2"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean1.setKeywordNum("3");
		keywordBean1.setMoveType("2");
		keywordBean1.setIsPrint("true");
		keywordBean1.setMoveSize(0);
		keywordBean1.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(keywordBean1);//关键字定位对象
		signRuleBeanList2.add(signRuleBean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	@Test
	public void testClientFileBatchSignByClient91() throws Exception {//客户端批量签章（支持全关键字,传签章图片base64编码）
		System.out.println("*******************");
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		byte[] fileBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN1 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN1 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String sealImg1 = "R0lGODlhgAAnAOMAAP8CAv8kZv9mJf9mZv86kP9mrP+QOv+QZv+2Zv+iqP+o5//opf/b2//b////2////yH5BAEAAA8ALAAAAACAACcAAAT+8MlJq70467qA98UmjmRpnqjVfUCYok4yCALrCYPy7vxlsITeJZaQOA42QILRkKwAQaE09fu4pk5PomYLNClPqHCR0GFHCJb5XE2qJ2HPGsYFeM+b9GcujRcYTwFyDnVvKXFyeBl6HwF8O3FmSG5dAwVfJgyFQIoYbYY8cQGElB8DOS+ISY94R6VRoTY6qnZLY6UArFJMrqUCRT1hsJ+OeLQBnRKbSZdYdWufVme9LFeKDriwUyvIWR+b2lLYe8lGpbo8MmbLq9MsmOWMNjTh27jSWGHoivKgbLgBgAU7wM5asgXsQHTqx6Jbj0m49u1gQDEhgF/Jxrmph4KhG4P+fQBKnKIpyTKQee6JubasWLkKCRqpRKmhjgBBDV8+EYjhiCUGPRx4vOewRJU/heC9fPCET0xOKZ7eCxKDhgeOItiNxPPsAQOIUluc0JgkQYM2WF9EK3qwzISEIeSlxcDQ5YM2NFOQLZX3BMI3e/f89YemmlK8uyDOlLKsgaovVT2wHeGzmQXEkFRS2qqhjQIuOqJ5UJrMcyzNSeaKeBKkSpRJBZouvUuOh+haTCYw2CKZR1fZDxwAZYqvnGkepGqRVtRV450KaF8y4jzbxHRvkhVgipSM0fLqpwFsR51IETfwzEdTSK6SuokVqtGj6IBVxjIcZsvV6Cu/v3QB7vkSJ+CABBZo4IEIJqjgggw2aEIEADs=";
		KeywordBean keywordBean = new KeywordBean();
		keywordBean.setTssCertSN(tssCertSN1);
		keywordBean.setSealImg(sealImg1);
		keywordBean.setCertSN(certSN1);
		keywordBean.setKeyword("seal");
		keywordBean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean.setKeywordNum("3");
		keywordBean.setMoveType("2");
		keywordBean.setIsPrint("true");
		keywordBean.setMoveSize(0);
		keywordBean.setHeightMoveSize(0);
		signRuleBean.setKeywordBean(keywordBean);//关键字定位对象
		signRuleBeanList.add(signRuleBean);
		
		clientSignMessage.setFileBty(fileBty);//签章文件
		clientSignMessage.setFileUniqueId("11111文件");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		byte[] fileBty2 = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String sealImg2 = "R0lGODlhgAAnAOMAAP8CAv8kZv9mJf9mZv86kP9mrP+QOv+QZv+2Zv+iqP+o5//opf/b2//b////2////yH5BAEAAA8ALAAAAACAACcAAAT+8MlJq70467qA98UmjmRpnqjVfUCYok4yCALrCYPy7vxlsITeJZaQOA42QILRkKwAQaE09fu4pk5PomYLNClPqHCR0GFHCJb5XE2qJ2HPGsYFeM+b9GcujRcYTwFyDnVvKXFyeBl6HwF8O3FmSG5dAwVfJgyFQIoYbYY8cQGElB8DOS+ISY94R6VRoTY6qnZLY6UArFJMrqUCRT1hsJ+OeLQBnRKbSZdYdWufVme9LFeKDriwUyvIWR+b2lLYe8lGpbo8MmbLq9MsmOWMNjTh27jSWGHoivKgbLgBgAU7wM5asgXsQHTqx6Jbj0m49u1gQDEhgF/Jxrmph4KhG4P+fQBKnKIpyTKQee6JubasWLkKCRqpRKmhjgBBDV8+EYjhiCUGPRx4vOewRJU/heC9fPCET0xOKZ7eCxKDhgeOItiNxPPsAQOIUluc0JgkQYM2WF9EK3qwzISEIeSlxcDQ5YM2NFOQLZX3BMI3e/f89YemmlK8uyDOlLKsgaovVT2wHeGzmQXEkFRS2qqhjQIuOqJ5UJrMcyzNSeaKeBKkSpRJBZouvUuOh+haTCYw2CKZR1fZDxwAZYqvnGkepGqRVtRV450KaF8y4jzbxHRvkhVgipSM0fLqpwFsR51IETfwzEdTSK6SuokVqtGj6IBVxjIcZsvV6Cu/v3QB7vkSJ+CABBZo4IEIJqjgggw2aEIEADs=";
		KeywordBean keywordBean1 = new KeywordBean();
		keywordBean1.setTssCertSN(tssCertSN2);
		keywordBean.setSealImg(sealImg2);
		keywordBean1.setCertSN(certSN2);
		keywordBean1.setKeyword("seal");
		keywordBean1.setSearchOrder("2"); // 检索顺序0:全部、 1:正序、2:倒序
		keywordBean1.setKeywordNum("3");
		keywordBean1.setMoveType("2");
		keywordBean1.setIsPrint("true");
		keywordBean1.setMoveSize(0);
		keywordBean1.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(keywordBean1);//关键字定位对象
		signRuleBeanList2.add(signRuleBean2);
		signRuleBeanList2.add(signRuleBean2);
		
		clientSignMessage2.setFileBty(fileBty2);//签章文件
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型(签章文件类型不设置默认签章文件类型为PDF)
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+"##状态码："+beanObj.getStatusCode()+"##状态信息："+beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
					ClientUtil.writeByteArrayToFile(new File("D:/batchSign/"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
//=================================以下是指定路径存储方式============================================		

	/**
	 * 客户端指定路径__并传所有签章参数基于坐标定位签章
	  * <p>testFileBatchSignByRectangleBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByRectangleBeanFilePath() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum = "1E69DBB775F2D6A8";//服务器印章编号
		RectangleBean bean=new RectangleBean();
		bean.setCertSN(certSN);
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		/*bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);*/
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径 Nas方式
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径Nas方式
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("test2文件");
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		RectangleBean bean2=new RectangleBean();
		String ruleNum2 = "7BEFAE090B557EBA";//签章规则编号
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum2 = "1E69DBB775F2D6A8";//服务器印章编号
		bean2.setCertSN(certSN2);
		bean2.setTssCertSN(tssCertSN2); //可选(不加时间戳则不设置)
		bean2.setServerSealNum(serverSealNum2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		/*bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);*/
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		//启用存储指定路径方式
		clientSignMessage2.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage2.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage2.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage2.setFileUniqueId("test3文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}
			
		}
	}
	
	/**
	 * 客户端指定路径__并传所有签章参数基于坐标定位签章,使用服务器印章绑定的签名策略
	 * <p>testFileBatchSignByRectangleBeanFilePath</p>
	 * @Description:
	 * @throws Exception
	 */

	@Test
	public void testFileBatchSignByRectangleBeanFilePathToServerSealNum() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN ="472A583F045CE94A";//时间戳签名策略52178EDD282F07D5
		String serverSealNum = "test1";//绑定签名策略的服务器印章编号
		RectangleBean bean=new RectangleBean();
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		/*bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);*/
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径 Nas方式
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径Nas方式
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("test2文件");
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}

		}
	}
	
	
	/**
	 * 客户端指定路径__并传所有签章参数基于关键字定位签章
	  * <p>testFileBatchSignByKeywordBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByKeywordBeanFilePath() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum = "1E69DBB775F2D6A8";//服务器印章编号
		KeywordBean bean = new KeywordBean();
		bean.setCertSN(certSN);
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		bean.setKeyword("合同编号");
		bean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		bean.setKeywordNum("3");
		bean.setMoveType("1");
		bean.setIsPrint("false");
		bean.setMoveSize(0);
		bean.setHeightMoveSize(0);
		signRuleBean.setKeywordBean(bean);
		signRuleBeanList.add(signRuleBean);
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径 Nas方式
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径Nas方式
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("test2文件");
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		KeywordBean bean2 = new KeywordBean();
		String ruleNum2 = "7BEFAE090B557EBA";//签章规则编号
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
		String serverSealNum2 = "1E69DBB775F2D6A8";//服务器印章编号
		bean2.setCertSN(certSN2);
		bean2.setTssCertSN(tssCertSN2); //可选(不加时间戳则不设置)
		bean2.setServerSealNum(serverSealNum2);
		bean2.setKeyword("合同编号");
		bean2.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		bean2.setKeywordNum("3");
		bean2.setMoveType("1");
		bean2.setIsPrint("false");
		bean2.setMoveSize(0);
		bean2.setHeightMoveSize(0);
		signRuleBean2.setKeywordBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		//启用存储指定路径方式
		clientSignMessage2.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage2.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage2.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage2.setFileUniqueId("test4文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}
			
		}
	}
	/**
	 * 客户端指定路径__并传所有签章参数基于关键字定位签章,使用服务器印章绑定的签名策略
	  * <p>testFileBatchSignByKeywordBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByKeywordBeanFilePathToServerSealNum() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String tssCertSN ="472A583F045CE94A";//时间戳签名策略52178EDD282F07D5
		String serverSealNum = "test3";//绑定签名策略的服务器印章编号
		KeywordBean bean = new KeywordBean();
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		bean.setKeyword("合同编号");
		bean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
		bean.setKeywordNum("3");
		bean.setMoveType("1");
		bean.setIsPrint("false");
		bean.setMoveSize(0);
		bean.setHeightMoveSize(0);
		signRuleBean.setKeywordBean(bean);
		signRuleBeanList.add(signRuleBean);
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径 Nas方式
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径Nas方式
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("test2文件");
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}
			
		}
	}

	
	/**
	 * 客户端指定路径__并传所有签章参数基于表单域定位签章
	  * <p>testFileBatchSignByFieldBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByFieldBeanFilePath() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean>signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		FieldBean bean = new FieldBean();
		String tssCertSN ="52178EDD282F07D5";//时间戳签名策略2226AE71EBF50AE6 
		String certSN = "53C130E7BF352E1F";//签名策略编号
		String serverSealNum = "1E69DBB775F2D6A8";//服务器印章编号
		bean.setCertSN(certSN);
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		bean.setKeyword("requestdate");
		bean.setIsPrint("true");
		bean.setMoveType("1");
		bean.setMoveSize(200);
		bean.setHeightMoveSize(0);
		signRuleBean.setFieldBean(bean);//表单域定位对象
		signRuleBeanList.add(signRuleBean);
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("test4文件");
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}
			
		}
	}
	
		/**
	 * 客户端指定路径__并传所有签章参数基于表单域定位签章,使用服务器印章绑定的签名策略
	  * <p>testFileBatchSignByFieldBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByFieldBeanFilePathToServerSealNum() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean>signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		FieldBean bean = new FieldBean();
		String tssCertSN ="52178EDD282F07D5";//时间戳签名策略2226AE71EBF50AE6 
		String serverSealNum = "1E69DBB775F2D6A8";//绑定签名策略的服务器印章编号
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		bean.setKeyword("requestdate");
		bean.setIsPrint("true");
		bean.setMoveType("1");
		bean.setMoveSize(200);
		bean.setHeightMoveSize(0);
		signRuleBean.setFieldBean(bean);//表单域定位对象
		signRuleBeanList.add(signRuleBean);
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("test4文件");
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}
			
		}
	}
	
	/**
	 * 客户端指定路径__并传入组合规则签章
	  * <p>testFileBatchSignByGroupFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
    @Test
	public void testFileBatchSignByGroupFilePath() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum ="53582C51C506201E";//签章规则编号 
		String tssCertSN ="52178EDD282F07D5";//时间戳签名策略
		String certSN = "53C130E7BF352E1F";//签名策略编号
		String serverSealNum = "1E69DBB775F2D6A8";//服务器印章编号
		RectangleBean bean = new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setCertSN(certSN);
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		clientSignMessage.setFileUniqueId("test1文件");
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		RectangleBean bean2 = new RectangleBean();
		String ruleNum2 = "35FDC50134878E0";//签章规则编号
		String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略 
		String certSN2 = "53C130E7BF352E1F";//签名策略编号
		String serverSealNum2 = "317AE35DDF4F9F86";//服务器印章编号
		bean2.setRuleNum(ruleNum2);
		bean2.setCertSN(certSN2);
		bean2.setTssCertSN(tssCertSN2); //可选(不加时间戳则不设置)
		bean2.setServerSealNum(serverSealNum2);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		clientSignMessage2.setFileUniqueId("test2文件");
		//启用存储指定路径方式
		clientSignMessage2.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage2.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage2.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+
                   beanObj.getSignUniqueId()+"##状态码："+
                   beanObj.getStatusCode()+"##状态信息："+
                   beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
				ClientUtil.writeByteArrayToFile(
             new File("D:\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	@Test
	public void testFileBatchSignByGroupFilePath1() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "62E02469B13B662B";//签章规则编号
		signRuleBean.setRuleNum(ruleNum);
		signRuleBeanList.add(signRuleBean);
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "53582C51C506201E";//签章规则编号
		signRuleBean2.setRuleNum(ruleNum2);
		signRuleBeanList.add(signRuleBean2);	
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("CS1111");
		messages.add(clientSignMessage);
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		//pdf字节数组
		SignRuleBean signRuleBean11=new SignRuleBean();
		String ruleNum11 = "62E02469B13B662B";//签章规则编号
		signRuleBean11.setRuleNum(ruleNum11);
		signRuleBeanList2.add(signRuleBean11);
		SignRuleBean signRuleBean22=new SignRuleBean();
		String ruleNum22 = "53582C51C506201E";//签章规则编号
		signRuleBean22.setRuleNum(ruleNum22);
		signRuleBeanList2.add(signRuleBean22);
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		//启用存储指定路径方式
		clientSignMessage2.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage2.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage2.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage2.setFileUniqueId("CS2222文件");
		messages.add(clientSignMessage2);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}
			
		}
	}
	/**
	 * 客户端指定路径__并传入签章规则及坐标定位签章
	  * <p>testFileBatchSignByRuleNumAndRectangleBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByRuleNumAndRectangleBeanFilePath() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		//pdf字节数组
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum = "62E02469B13B662B";//签章规则编号
		RectangleBean bean=new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setPageNo(1);
		bean.setLeft(200f);
		bean.setTop(300f);
		bean.setRight(300f);
		bean.setBottom(200f);
		/*bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);*/
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setFileUniqueId("1111文件");
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		//pdf字节数组
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		String ruleNum2 = "53582C51C506201E";//签章规则编号
		RectangleBean bean2=new RectangleBean();
		bean2.setRuleNum(ruleNum2);
		bean2.setPageNo(1);
		bean2.setLeft(200f);
		bean2.setTop(300f);
		bean2.setRight(300f);
		bean2.setBottom(200f);
		/*bean.setIsPrint("false");
		bean.setMoveType("1");//中心点坐标只能1覆盖
		bean.setMoveSize(300);
		bean.setHeightMoveSize(0);*/
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		//启用存储指定路径方式
		clientSignMessage2.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage2.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage2.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage2.setFileUniqueId("2222文件");
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		List<ClientSignBean> clientSignBeanList= message.getClientSignList();
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
			System.out.println("服务端批量签章结果列表：");
			for (ClientSignBean beanObj : clientSignBeanList) {
				System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
			}
		}
	}
	
	/**
	 * 客户端指定路径__并传入签章规则及坐标定位签章
	  * <p>testFileBatchSignByRuleNumAndKeywordBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByRuleNumAndKeywordBeanFilePath() throws Exception {
			List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
			ClientSignMessage clientSignMessage = new ClientSignMessage();
			//pdf字节数组
			List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
			SignRuleBean signRuleBean=new SignRuleBean();
			String tssCertSN ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
			String certSN = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
			String serverSealNum = "1E69DBB775F2D6A8";//服务器印章编号
			KeywordBean bean = new KeywordBean();
			String ruleNum = "7BEFAE090B557EBA";//签章规则编号
			bean.setRuleNum(ruleNum);
			bean.setKeyword("合同编号");
			bean.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
			bean.setKeywordNum("3");
			bean.setMoveType("1");
			bean.setIsPrint("false");
			bean.setMoveSize(0);
			bean.setHeightMoveSize(0);
			signRuleBean.setKeywordBean(bean);
			signRuleBeanList.add(signRuleBean);
			//启用存储指定路径方式
			clientSignMessage.setIsUserStorage(true);
			//输入文件路径
			clientSignMessage.setDocFilePath(testPdfFilePath);
			//输出文件路径
			clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
			clientSignMessage.setFileUniqueId("1111文件");
			clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
			messages.add(clientSignMessage);
			ClientSignMessage clientSignMessage2 = new ClientSignMessage();
			//pdf字节数组
			List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
			SignRuleBean signRuleBean2=new SignRuleBean();
			KeywordBean bean2 = new KeywordBean();
			String ruleNum2 = "7BEFAE090B557EBA";//签章规则编号
			String tssCertSN2 ="52178EDD282F07D5";//时间戳签名策略52178EDD282F07D5 
			String certSN2 = "53C130E7BF352E1F";//签名策略编号53C130E7BF352E1F
			String serverSealNum2 = "1E69DBB775F2D6A8";//服务器印章编号
			bean2.setRuleNum(ruleNum2);
			bean2.setKeyword("合同编号");
			bean2.setSearchOrder("0"); // 检索顺序0:全部、 1:正序、2:倒序
			bean2.setKeywordNum("3");
			bean2.setMoveType("1");
			bean2.setIsPrint("false");
			bean2.setMoveSize(0);
			bean2.setHeightMoveSize(0);
			signRuleBean2.setKeywordBean(bean2);
			signRuleBeanList2.add(signRuleBean2);
			//启用存储指定路径方式
			clientSignMessage2.setIsUserStorage(true);
			//输入文件路径
			clientSignMessage2.setDocFilePath(testPdfFilePath);
			//输出文件路径
			clientSignMessage2.setDocOutFilePath(testSignedPdfFilePath);
			clientSignMessage2.setFileUniqueId("2222文件");
			clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
			messages.add(clientSignMessage2);
			ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
			System.out.println("状态码：" + message.getStatusCode());
			System.out.println("状态信息："+message.getStatusInfo());
			if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> clientSignBeanList= message.getClientSignList();
			System.out.println("状态码："+message.getStatusCode());
			System.out.println("状态信息："+message.getStatusInfo());
			if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
				System.out.println("服务端批量签章结果列表：");
				for (ClientSignBean beanObj : clientSignBeanList) {
					System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
				}
			}
		}
	}
	
	/**
	 * 客户端指定路径__并传入签章规则及坐标定位签章
	  * <p>testFileBatchSignByRuleNumAndKeywordBeanFilePath</p>
	  * @Description:
	  * @throws Exception
	 */
	
	@Test
	public void testFileBatchSignByRuleNumAndFieldBeanFilePath() throws Exception {
			List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
			ClientSignMessage clientSignMessage = new ClientSignMessage();
			List<SignRuleBean>signRuleBeanList=new ArrayList<SignRuleBean>();
			SignRuleBean signRuleBean=new SignRuleBean();
			String ruleNum = "7BEFAE090B557EBA";//签章规则编号
			FieldBean bean = new FieldBean();
			bean.setRuleNum(ruleNum);
			bean.setKeyword("requestdate");
			bean.setIsPrint("true");
			bean.setMoveType("1");
			bean.setMoveSize(200);
			bean.setHeightMoveSize(0);
			signRuleBean.setFieldBean(bean);//表单域定位对象
			signRuleBeanList.add(signRuleBean);
			clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
			//启用存储指定路径方式
			clientSignMessage.setIsUserStorage(true);
			//输入文件路径
			clientSignMessage.setDocFilePath(testPdfFilePath);
			//输出文件路径
			clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
			clientSignMessage.setFileUniqueId("test4文件");
			messages.add(clientSignMessage);
			ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
			System.out.println("状态码：" + message.getStatusCode());
			System.out.println("状态信息："+message.getStatusInfo());
			if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> clientSignBeanList= message.getClientSignList();
			System.out.println("状态码："+message.getStatusCode());
			System.out.println("状态信息："+message.getStatusInfo());
			if("200".equals(message.getStatusCode()) && clientSignBeanList.size()>0){//成功
				System.out.println("服务端批量签章结果列表：");
				for (ClientSignBean beanObj : clientSignBeanList) {
					System.out.println("签章流水:"+beanObj.getSignUniqueId()+",##状态码："+beanObj.getStatusCode()+",##状态信息："+beanObj.getStatusInfo()+",##签章后的pdf保存到指定存储路径地址："+beanObj.getDocOutFilePath());
				}
			}
		}
	}
	
	
	@Test //传入组合规则签章、坐标规则、关键字规则、倒序  
	public void testFileBatchSignByGroupFilePath04() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum ="53582C51C506201E";//签章规则编号   坐标 SM2  仅允许复制
		String tssCertSN ="";//时间戳签名策略
		String certSN = "53C130E7BF352E1F";//签名策略编号 SM2
		String serverSealNum = "1E69DBB775F2D6A8";//服务器印章编号  png logo
		RectangleBean bean = new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setCertSN(certSN);
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		clientSignMessage.setFileUniqueId("111111");//test1文件
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		RectangleBean bean2 = new RectangleBean();
		String ruleNum2 = "6DB1D1F340D41E92";//签章规则编号  
		String tssCertSN2 ="";//时间戳签名策略  RSA  52178EDD282F07D5
		String certSN2 = "1BC295050EFF499C";//签名策略编号    RSA2048
		String serverSealNum2 = "10AE1D594699A473";//服务器印章编号   
		bean2.setRuleNum(ruleNum2);
		bean2.setCertSN(certSN2);
		bean2.setTssCertSN(tssCertSN2); //可选(不加时间戳则不设置)
		bean2.setServerSealNum(serverSealNum2);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		clientSignMessage2.setFileUniqueId("222222");//test2文件
		//启用存储指定路径方式
		clientSignMessage2.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage2.setDocFilePath(testPdfFilePath);
		//输出文件路径
		clientSignMessage2.setDocOutFilePath(testSignedPdfFilePath);
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+
                   beanObj.getSignUniqueId()+"##状态码："+
                   beanObj.getStatusCode()+"##状态信息："+
                   beanObj.getStatusInfo());
				if("200".equals(beanObj.getStatusCode())){
				ClientUtil.writeByteArrayToFile(
             new File("D:\\"+beanObj.getSignUniqueId()+".pdf"), beanObj.getPdfBty());
				}
			}
		}
	}
	
	
	@Test  //组合2个规则，都是坐标    本地存储走NAS  
    public void testFileSignByParam0501() throws Exception {
		List<ClientSignMessage> messages = new ArrayList<ClientSignMessage>();
		ClientSignMessage clientSignMessage = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean=new SignRuleBean();
		String ruleNum ="zhangqinkeyword";//签章规则编号 
		String tssCertSN ="DB3671B9FEFB036";//时间戳签名策略
		String certSN = "7E31AB4AB45686FA";//签名策略编号
		String serverSealNum = "bmp";//服务器印章编号
		RectangleBean bean = new RectangleBean();
		bean.setRuleNum(ruleNum);
		bean.setCertSN(certSN);
		bean.setTssCertSN(tssCertSN); //可选(不加时间戳则不设置)
		bean.setServerSealNum(serverSealNum);
		signRuleBean.setRectangleBean(bean);
		signRuleBeanList.add(signRuleBean);
		clientSignMessage.setFileUniqueId("111111");
		//启用存储指定路径方式
		clientSignMessage.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage.setDocFilePath("/mnt/test/179K.pdf");
		//输出文件路径
		clientSignMessage.setDocOutFilePath("/mnt/test/test_out0501.pdf");
		clientSignMessage.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage.setSignRuleBeanList(signRuleBeanList);//签章参数集合
		messages.add(clientSignMessage);
		
		ClientSignMessage clientSignMessage2 = new ClientSignMessage();
		List<SignRuleBean> signRuleBeanList2=new ArrayList<SignRuleBean>();
		SignRuleBean signRuleBean2=new SignRuleBean();
		RectangleBean bean2 = new RectangleBean();
		String ruleNum2 = "zuobiao";//签章规则编号
		String tssCertSN2 = "";//时间戳签名策略 
		String certSN2 = "102sm2";//签名策略编号
		String serverSealNum2 = "wfs";//服务器印章编号
		bean2.setRuleNum(ruleNum2);
		bean2.setCertSN(certSN2);
		bean2.setTssCertSN(tssCertSN2); //可选(不加时间戳则不设置)
		bean2.setServerSealNum(serverSealNum2);
		signRuleBean2.setRectangleBean(bean2);
		signRuleBeanList2.add(signRuleBean2);
		clientSignMessage2.setFileUniqueId("222222");
		//启用存储指定路径方式
		clientSignMessage2.setIsUserStorage(true);
		//输入文件路径
		clientSignMessage2.setDocFilePath("/mnt/test/179K.pdf");
		//输出文件路径
		clientSignMessage2.setDocOutFilePath("/mnt/test/test_out0502.pdf");
		clientSignMessage2.setEntityType(ENTITYType.PDF);//签章文件类型
		clientSignMessage2.setSignRuleBeanList(signRuleBeanList2);//签章参数集合
		messages.add(clientSignMessage2);
		ChannelMessage message = anySignClientTool.pdfBatchSignCByParam(messages);
		System.out.println("状态码：" + message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){ // 成功，获取签章文件
			List<ClientSignBean> signBeanList = message.getClientSignList();
			System.out.println("签章结果列表：");
			for (ClientSignBean beanObj : signBeanList) {
				System.out.println("签章流水:"+
                   beanObj.getSignUniqueId()+"##状态码："+
                   beanObj.getStatusCode()+"##状态信息："+
                   beanObj.getStatusInfo());
			}
		}
	}
	
}

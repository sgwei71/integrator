package com.ibkglobal.integrator.mms;

import java.io.IOException ;
import java.io.StringWriter ;
import java.util.Collection ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.Map ;

import javax.servlet.ServletContext ;
import javax.servlet.ServletException ;
import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.Part ;

import org.apache.commons.io.IOUtils ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.jdom2.Document ;
import org.jdom2.Element ;
import org.jdom2.output.Format ;
import org.jdom2.output.XMLOutputter ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext ;
import org.springframework.web.context.support.WebApplicationContextUtils ;

public class MmsServlet extends HttpServlet {
	  private static final Logger logger = LoggerFactory.getLogger(MmsServlet.class) ;
	  private static final long serialVersionUID = 1L ;

	  public MmsServlet()
	  {
	  }

	  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
		  logger.debug("******************************************* doGet Start *******************************************") ;
	  }

	  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	  {
	    String message = "" ;
	    String rscode = "" ;
	    try
	    {

	      Log log = LogFactory.getLog(this.getClass()) ;

	      log.debug(IOUtils.toBufferedInputStream(req.getInputStream())) ;

	      /**
	       * multipart/form-data로 넘어온 데이터를 받는다.
	       */
	      String[] names = { "logInfoString", "interface", "source-io", "target-io", "inbound-mapping", "outbound-mapping"} ;
	      //String[] names = { "interface", "source-io", "target-io", "inbound-mapping", "outbound-mapping", } ;
	      Map<String, byte[]> multipart = parserMultiPart(req) ;
	      
	      // key 값으로 데이터를 찾는다.
	      byte[] mmsLogInfoString = multipart.get(names[0]);
	      byte[] mmsInterface = multipart.get(names[1]) ;
	      byte[] mmsSourceIO = multipart.get(names[2]) ;
	      byte[] mmsTargetIO = multipart.get(names[3]) ;
	      byte[] mmsInboundMapping = multipart.get(names[4]) ;
	      byte[] mmsOutbundMapping = multipart.get(names[5]) ;

	      ServletContext servlet = getServletContext() ;
	      ApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servlet) ;

	      /**
	       * MmsService를 호출한다.
	       */
	      processMMS(mmsLogInfoString, mmsInterface, mmsSourceIO, mmsTargetIO, mmsInboundMapping, mmsOutbundMapping, wac) ;

	       rscode = "0";
//	      rscode = "-1" ;
	     //성공 메시지 작성
	      message = "배포 정상처리 되었습니다." ;
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace() ;
	      //배포 실패 메시지 작성
	      message = "배포 오류  " + e.getMessage() ;
	      rscode = "-1" ;
	    }

	    /**
	     * http Response-------------------------------------------
	     */

	    // MMS xml 응답 내용 생성

	    String xmlResult = mkXmlStream(rscode, message) ;

	    logger.debug("//////////////////////응답 내용/////////////////////////////") ;
	    logger.debug(xmlResult) ;
	    logger.debug("///////////////////////////////////////////////////////////") ;

	    // MMS로 응답내용 전송
	    resp.setContentType("text/xml; charset=EUC-KR") ;

	    IOUtils.write(xmlResult, resp.getOutputStream()) ;
	  }

	  private void processMMS(byte[] mmsLogInfoString, byte[] mmsInterface, byte[] mmsSourceIO, byte[] mmsTargetIO,
			byte[] mmsInboundMapping, byte[] mmsOutbundMapping, ApplicationContext wac) {
		// TODO Auto-generated method stub
		
	}

	/**
	   * 
	   * 응답 xml메시지를 생성합니다.
	   * <p>
	   * 
	   * @author I25244, 2013. 4. 2.
	   * @param rscode
	   * @param message
	   * @return result.toString()
	   * @throws IOException
	   */
	  public static String mkXmlStream(String rscode, String message) throws IOException
	  {
	    // Document to StringStream
	    Document document = new Document() ;

	    // XML 양식 생성
	    Element root = new Element("response") ;
	    root.setAttribute("code", rscode) ;
	    document.addContent(root) ;
	    Element eliment1 = new Element("message") ;
	    eliment1.setText(message) ;
	    root.addContent(eliment1) ;

	    StringWriter result = new StringWriter() ;
	    // XML 파일 생성
	    XMLOutputter outputter = new XMLOutputter() ;
	    // 출력 포맷 설정
	    Format fm = outputter.getFormat() ;
	    // Encoding 형태 설정
	    fm.setEncoding("EUC-KR") ;
	    // 부모 자식 태그 구별 탭 범위 설정
	    fm.setIndent("    ") ;
	    // 태그끼리 줄바꿈 설정
	    fm.setLineSeparator("\r\n") ;
	    // XML문서에 잇는 문자열 TRIM(공백제거)
	    fm.setTextMode(Format.TextMode.TRIM) ;
	    outputter.setFormat(fm) ;

	    // String 방식으로 저장
	    outputter.output(document, result) ;
	    return result.toString() ;
	  }

	  private Map<String, byte[]> parserMultiPart(HttpServletRequest req) throws Exception
	  {

	    Map<String, byte[]> result = new HashMap<String, byte[]>() ;
	    
	    try
	    {

	      Collection<Part> parts = req.getParts() ;
	      Iterator<Part> part = parts.iterator() ;
	      while (part.hasNext())
	      {
	        Part p = (Part) part.next() ;
	        for (String headerName : p.getHeaderNames())
	        {
	          System.out.println(p.getHeader(headerName)) ;
	        }

	        // part xml 네임을 받아옴
	        String name = p.getName() ;
	        System.out.println(name) ;
	        
	        // InputStream 데이터를 byte 로 형변환
	        byte[] content = IOUtils.toByteArray(p.getInputStream()) ;

	        // name 은 key 값, content 는 데이터
	        result.put(name, content) ;
	      }

	      return result ;

	    }
	    catch (Exception e)
	    {
	      throw new Exception("MMS Multi Part Process Error[" + e.getMessage() + "]", e) ;
	    }

	  }
}

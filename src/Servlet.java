import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.io.*;
import java.util.Iterator;
import java.util.List;
/**
 * @author liqi
 * @created on 2018/1/16
 **/
@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    private boolean isMultipart;
    private String filePath;
    private String filename;
    private int maxFileSize = 50 * 1024*1024;
    private int maxMemSize = 6 * 1024*1024;
    private File file ;
    String f1path;
    String f2path;
    @Override
    public void init( ){
       filePath = getServletContext().getInitParameter("file-upload");
    }
    void  doPython(){
        try {
            //需传入的参数
            //设置命令行传入参数
            //String[] arg = new String[]{"python E:\\submit\\web\\WEB-INF\\submit\\submit.py"};
            Process pr = Runtime.getRuntime().exec("python  E:\\submit_ajax\\web\\submit\\submit.py");
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject temp = new JSONObject();
       isMultipart = ServletFileUpload.isMultipartContent(request);
      /*  if( !isMultipart ){
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>No file uploaded</p>");
            out.println("</body>");
            out.println("</html>");
            return;
        }*/
        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置最大的文件接收大小
            factory.setSizeThreshold(maxMemSize);
            // 超过最大接收时的保存位置
            factory.setRepository(new File("E:\\submit_ajax\\web\\submit\\test data"));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //上传文件
            upload.setSizeMax( maxFileSize );
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);
            // Process the uploaded file items
            Iterator i = fileItems.iterator();
            while ( i.hasNext () )
            {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () )
                {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    // Write the file
                    if( fileName.lastIndexOf("\\") >= 0 ){
                        file = new File( filePath +
                                fileName.substring( fileName.lastIndexOf("\\"))) ;
                    }else{
                        file = new File( filePath +
                                fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                    }
                    fi.write( file ) ;
                }
            }
        } catch (Exception e) {}
         doPython();
            String ss = new String(file.getPath());
            String outpath = ss.substring(ss.lastIndexOf("\\") + 1);
            File f1 = new File(ss);
            File f2 = new File("E:\\submit_ajax\\web\\submit\\results_box\\" + outpath);
           if (f1.exists() && f2.exists()) {
                f1path = "E:\\submit_ajax\\web\\images\\test\\" + f2.getName();
                f2path = "E:\\submit_ajax\\web\\images\\result\\" + f2.getName();
                if (f1.renameTo(new File(f1path))) {
                    f1.delete();
                    temp.put("f1path","picture1/"+f2.getName());
                    System.out.println(1);
                }
                if (f2.renameTo(new File(f2path))) {
                    f2.delete();
                    temp.put("f2path","picture/"+f2.getName());
                    System.out.println(2);
                }
                array.add(0,temp);
                json.put("file",array);
         }
         response.setContentType("application/json");
           out.println(json);
           out.flush();
          out.close();
    }
   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
/*class JsonReader {
    static JSONObject receivePost(HttpServletRequest request) throws IOException, UnsupportedEncodingException {
        // 读取请求内容
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine())!= null) {
            sb.append(line);
        }
        //将json字符串转换为json对象
        JSONObject json=JSONObject.fromObject(sb.toString());
        return json;
    }
}*/

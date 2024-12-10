import check.Checker;
import com.fastcgi.FCGIInterface;
import validation.Validate;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;


public class Server {
    public static void main(String[] args) throws IOException {
        var fcgiInterface = new FCGIInterface();
        Validate validate = new Validate();
        Checker check = new Checker();

        while (fcgiInterface.FCGIaccept() >= 0) {
            String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
            if (method.equals("POST")){
                long workTime = System.nanoTime();
                String req = readRequestBody();
                if(!req.equals("")){
                    LinkedHashMap<String, String> m = getValues(req);
                    boolean isHit;
                    boolean isValid;
                    try{
                        isValid = validate.check(Integer.parseInt(m.get("x")), Float.parseFloat(m.get("y")), Float.parseFloat(m.get("r")));
                        isHit = check.isHit(Integer.parseInt(m.get("x")), Float.parseFloat(m.get("y")), Float.parseFloat(m.get("r")));
                    }catch (Exception e){
                        System.out.println(err("Invalid data"));
                        continue;
                    }
                    if (isValid){
                        System.out.println(resp(isHit, m.get("x"), m.get("y"), m.get("r"), workTime));
                    }
                    else System.out.println(err(validate.getErr()));
                }
                else System.out.println(err("fill"));
            }
            else System.out.println(err("method"));
        }
    }

    private static String readRequestBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes =
                FCGIInterface.request.inStream.read(buffer.array(), 0,
                        contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    private static LinkedHashMap<String, String> getValues(String inpString){
        String[] args = inpString.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for(String s : args){
            String[] arg = s.split("=");
            map.put(arg[0], arg[1]);
        }
        return map;
    }

    private static String resp(boolean isHit, String x, String y, String r, long workTime){
        String content = """
                        {"hit":"%s","x":"%s","y":"%s","r":"%s","time":"%s","workTime":"%s","error":"all ok"}
                        """.formatted(isHit, x, y, r, (double)(System.nanoTime()-workTime) / 1000000, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return """
               Content-Type: application/json charset=utf-8
               Content-Length: %d
                       
                       
               %s
               """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
    }

    private static String err(String msg) {
        String content = """
                {"error":"%s"}
                """.formatted(msg);
        return """
               Content-Type: application/json charset=utf-8
               Content-Length: %d
               
               
               %s 
               """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
    }
}

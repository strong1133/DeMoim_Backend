package com.demoim_backend.demoim_backend.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class RandomImg {
    String imgUrl;
    public String rndImg(int rNum){
        if (rNum == 0){
            imgUrl = "https://cdn.pixabay.com/photo/2015/01/09/11/08/startup-594090_1280.jpg";
        }else if(rNum == 1){
            imgUrl = "https://images.unsplash.com/photo-1566140967404-b8b3932483f5?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2850&q=80";
        } else if(rNum == 2){
            imgUrl = "https://cdn.pixabay.com/photo/2018/03/10/12/00/paper-3213924_1280.jpg";
        }else if(rNum == 3){
            imgUrl = "https://cdn.pixabay.com/photo/2015/01/08/18/27/startup-593341_1280.jpg";
        }else if(rNum == 4){
            imgUrl = "https://cdn.pixabay.com/photo/2019/09/25/09/36/team-4503157_1280.jpg";
        }else if(rNum == 5){
            imgUrl = "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80";
        }else if(rNum == 6){
            imgUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80";
        }else if(rNum == 7){
            imgUrl = "https://images.unsplash.com/photo-1497215728101-856f4ea42174?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1650&q=80";
        }else if(rNum == 8){
            imgUrl = "https://images.unsplash.com/photo-1460794418188-1bb7dba2720d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1650&q=80";
        }else if(rNum == 9){
            imgUrl = "https://images.unsplash.com/photo-1492551557933-34265f7af79e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2850&q=80";
        }else if(rNum == 10){
            imgUrl = "https://images.unsplash.com/photo-1512758017271-d7b84c2113f1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80";
        }else if(rNum == 11){
            imgUrl = "https://images.unsplash.com/photo-1516321497487-e288fb19713f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1350&q=80";
        }else if(rNum == 12){
            imgUrl = "https://images.unsplash.com/photo-1553877522-43269d4ea984?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1350&q=80";
        }else if(rNum == 13){
            imgUrl = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1330&q=80";
        }else if(rNum == 14){
            imgUrl = "https://images.unsplash.com/photo-1542181961-9590d0c79dab?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";
        }else if(rNum == 15){
            imgUrl = "https://images.unsplash.com/photo-1541746972996-4e0b0f43e02a?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";
        }
        return imgUrl;
    };
}

//"https://cdn.pixabay.com/photo/2015/01/09/11/08/startup-594090_1280.jpg",
//"https://cdn.pixabay.com/photo/2018/03/10/12/00/paper-3213924_1280.jpg",
//"https://cdn.pixabay.com/photo/2015/01/08/18/27/startup-593341_1280.jpg",
//"https://cdn.pixabay.com/photo/2019/09/25/09/36/team-4503157_1280.jpg"
//https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80
//https://images.unsplash.com/photo-1522071820081-009f0129c71c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80
//https://images.unsplash.com/photo-1497215728101-856f4ea42174?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1650&q=80
//https://images.unsplash.com/photo-1460794418188-1bb7dba2720d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1650&q=80
//https://images.unsplash.com/photo-1492551557933-34265f7af79e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2850&q=80
//https://images.unsplash.com/photo-1512758017271-d7b84c2113f1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80
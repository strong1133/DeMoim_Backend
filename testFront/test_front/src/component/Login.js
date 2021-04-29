import React, {useState, useEffect} from 'react';
import axios from "axios";
import styled from 'styled-components';
import { Link } from 'react-router-dom';

function Login(){
    const [users, setUsers] = useState([]);

    const fetchUsers = async () => {

        const response = await axios.get(
            'http://localhost:8080/api/hello'
        );
        setUsers(response.data);
        console.log(response)
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    return (

        <FormWrap>
            <LoginTitle >
                <h2>로그인 하기</h2>
            </LoginTitle>
            <LoginForm>
                
                <div>
                    <Input className="idinput" placeholder="아이디"></Input>
                </div>
                
                <div>
                    <Input placeholder="패스워드"></Input>
                </div>

                <div>
                    
                    <Button > 로그인</Button>
                    <Link to="/signup"><Button to="/signup" className="signupBtn">회원가입</Button></Link>
                    
                </div>
            </LoginForm>
        </FormWrap>

    );
}

const FormWrap = styled.div`
    max-width:100vw;
    height:100vh;
    background-color: #ebebeb;
    padding: 15px;
    margin: 0;
`;
const LoginTitle = styled.div`
    width: 350px;
    margin: 100px auto 0;
    text-align:center;
`;

const LoginForm = styled.div`
    width: 350px;
    margin: 0 auto;

    border: 1px solid dimgray;
    border-radius: 5px;
    background-color: white;
`;

const Input = styled.input`
    width: 345px;
    height: 40px;
    border: none;
    outline:none;
    border-bottom: 1px solid dimgray;   
    background-color: whitesmoke;
`;

const Button = styled.button`
    border:none;
    width: 350px;
    background-color: #5645a8;
    outline:none;
    color:white;
    font-size: 18px;
    font-weight: 900;
    height: 40px;
    &:hover{
        background-color: #695bb3;
    }
    &.signupBtn{
        background-color: salmon;
        &:hover{
            background-color: darksalmon;
        }
    }
    
`;

export default Login;
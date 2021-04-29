import React, {useState, useEffect, Component} from 'react';
import axios from "axios";
import styled from 'styled-components';
import { Link } from 'react-router-dom';



class Signup extends Component {
    
    /* id password state값 으로 정의 */
    state = {
      id: '',
      password: '',
      name:'',
      stack:''
    }

    /* input value 변경 ==> onChange */
    appChange = (e) => {
      this.setState({
        [e.target.name]: e.target.value
      });
    }

    /* 로그인 버튼 클릭 ==> onClick */
    appClick = () => {
        const id = this.state.id;
        let password = this.state.password;
        let name = this.state.name;
        let stack = this.state.stack;

      console.log(`id는 : ${this.state.id}\npw는 : ${this.state.password}`);
      console.log(`name는 : ${this.state.name}\nstack는 : ${this.state.stack}`);
      
      axios({
        method:"POST",
        url: 'http://localhost:8080/api/signup',
        data:{
            username:id,
            password:password,
            stack:stack
        }
    }).then((res)=>{
        console.log(res);
    }).catch(error=>{
        console.log(error);
        throw new Error(error)
    })
    }
    appKeyPress = (e) => {
      if (e.key === 'Enter') {
        this.appClick();
      }
    }
    render() {
      const { id, password, name, stack } = this.state;
      const { appChange, appClick, appKeyPress } = this;
      return (

        <FormWrap>
            <LoginTitle >
                <h2>회원가입 하기</h2>
            </LoginTitle>
            <LoginForm >
                    <div>
                        <Input type="text" name="id" placeholder="아이디" value={id} onChange={appChange} />
                    </div>
                    
                    <div>
                        <Input type="password" name="password" placeholder="비밀번호" value={password} onChange={appChange} onKeyPress={appKeyPress}/>
                    </div>
    
                    <div>
                        <Input type="name" name="name" placeholder="이름" value={name} onChange={appChange} onKeyPress={appKeyPress}/>
                    </div>
    
                    <div>
                        <Input type="stack" name="stack" placeholder="주특기" value={stack} onChange={appChange} onKeyPress={appKeyPress}/>
                    </div>
    
                    <div>
                        <Button onClick={appClick}>완료</Button>
                        <Link to="/login"><Button className="signupBtn">취소</Button></Link>
                    </div>
                </LoginForm>
        </FormWrap>
    );
    }
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

export default Signup;
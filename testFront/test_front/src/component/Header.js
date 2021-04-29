import React from 'react';
import styled from 'styled-components';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngrycreative } from "@fortawesome/free-brands-svg-icons";
import { faKey } from "@fortawesome/free-solid-svg-icons";
import { Link } from 'react-router-dom';

const Header = () =>{
    return(
        <HeaderWrap>
            <div style={{cursor: 'pointer'}}>
                <FontAwesomeIcon icon={faAngrycreative} size="2x" style={{marginRight: '10px', marginTop :'3px'}}/>
                <NavTitle>SJ Test View</NavTitle>
            </div>
            <LoginBtn>
                <Link to="/login" style={{color:'white'}}><FontAwesomeIcon icon={faKey} style={{ marginRight: '20px', marginTop :'10px'}} /></Link>
            </LoginBtn>
        </HeaderWrap>


    )
}



const HeaderWrap = styled.div`
    display: flex;
    justify-content: space-between;
    padding: 10px;
    line
    max-width:100%;
    height: 38px;

    background-color: #5645a8;
    color: white;
    font-weight: 900;


    
`;
const NavTitle = styled.span`
    position: relative;
    top: -6px
`;

const LoginBtn = styled.div`
    cursor: pointer;
    font-size:20px;
    &:hover{
        color: cadetblue;
        transition: all 220ms ease
    }
`;


export default Header;
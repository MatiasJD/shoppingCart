import { Component, OnInit } from '@angular/core';
import {LoginService} from '../service/login.service';
import {Router} from '@angular/router';
import {error} from 'selenium-webdriver';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email;
  password;
  message;

  constructor(private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
  }

  login(){
    this.loginService.getUser(this.email, this.password).subscribe( user => {
      console.log(user)
      if (user){
        this.loginService.authenticated = true;
        this.loginService.email= user.email;
        this.loginService.password= user.password;
        this.message = null;
        this.router.navigate(['shoppingCart']);
      }
    },error => {
      console.log(error.error.message);
      this.message = error.error.message;
    });
  }

  changeMessage(){
    this.message = false;
  }

}

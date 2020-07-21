import { Component, OnInit } from '@angular/core';
import {LoginService} from '../service/login.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email;
  password;

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
        this.router.navigate(['shoppingCart']);
      }
    });
  }

}

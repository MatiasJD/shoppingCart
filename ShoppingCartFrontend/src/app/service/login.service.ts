import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable} from 'rxjs/index';
import {environment} from '../../environments/environment';

@Injectable()
export class LoginService {

  baseUrl: string = environment.baseUrl;
  authenticated = false;
  email: string;
  password: string;

  constructor(private http: HttpClient) { }

  getUser( email , password): Observable<any> {
    return this.http.get(this.baseUrl + '/getUser/' + email);
  }

}

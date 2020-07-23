import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientReportsService {

  baseUrl: string = environment.baseUrl;

  constructor(private http: HttpClient) { }

  getClientesVip(): Observable<any> {
    return this.http.get(this.baseUrl + '/clientsVip');
  }

  getClientesNoVipPorMes(date): Observable<any> {
    return this.http.get(this.baseUrl + '/clientsVipEndDate?date=' + date);
  }

  getClientesVipPorMes(date): Observable<any> {
    return this.http.get(this.baseUrl + '/clientsVipStartDate?date=' + date);
  }

}

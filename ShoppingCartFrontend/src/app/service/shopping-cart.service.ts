import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/index';
import {environment} from '../../environments/environment';

@Injectable()
export class ShoppingCartService {

  baseUrl: string = environment.baseUrl;
  cart: any;
  product: any;

  constructor(private http: HttpClient) {  }

  getCart(email: string): Observable<any>{
    return this.http.get(this.baseUrl + '/getCart/' + email);
  }

  saveProductToCart(product, cart): Observable<any>{
    let wrapper = {
      cart:cart,
      product:product,
      cant: product.cant
    };
    return this.http.post(this.baseUrl + '/saveCart', wrapper);
  }

  updateCart(): Observable<any>{
    return this.http.post(this.baseUrl + '/updateCart', this.cart);
  }

  deleteCartProduct(cartProduct): Observable<any>{
    return this.http.delete(this.baseUrl + '/deleteCartProduct/' + cartProduct);
  }

  deleteCart(): Observable<any>{
    return this.http.delete(this.baseUrl + '/deleteCart/' + this.cart.id);
  }

  finishBuy(): Observable<any>{
    return this.http.get(this.baseUrl + '/finishBuy/' + this.cart.id);
  }

}

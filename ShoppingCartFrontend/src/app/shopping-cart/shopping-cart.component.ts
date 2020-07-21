import {Component, Input, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {ShoppingCartService} from '../service/shopping-cart.service';
import {LoginService} from '../service/login.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {
  
  cart: any;

  constructor(private shoppingCartService: ShoppingCartService, private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    if (this.loginService.authenticated){

      if (this.shoppingCartService.cart){
        this.cart = this.shoppingCartService.cart;
        this.verifyProduct();

      } else {
        this.shoppingCartService.getCart(this.loginService.email).subscribe(cart =>{
          this.cart = cart;
          this.shoppingCartService.cart = cart;
        });
        this.verifyProduct();
      }
    }else {
      this.router.navigate(['/login']);
    }
  }

  verifyProduct(){
    let product = this.shoppingCartService.product;

    if (product) {

      let contains = false;

      for(let element of this.cart.cartProducts){
        if(element.product.id === product.id){

          element.cant += product.cant;
          contains = true;

          this.shoppingCartService.updateCart().subscribe(cart => {
            this.cart = cart;
            this.shoppingCartService.cart = cart;
            this.shoppingCartService.product = null;
          })
          break;
        }
      }

      if(!contains){
        this.shoppingCartService.saveProductToCart(product, this.cart).subscribe( cart => {
          this.cart = cart;
          this.shoppingCartService.cart = cart;
          this.shoppingCartService.product = null;
        });
      }
    }
  }

  deleteCartProduct(cartProduct){
    this.shoppingCartService.deleteCartProduct(cartProduct).subscribe(cart => {
      this.cart = cart;
      this.shoppingCartService.cart = cart;
    });
  }



}

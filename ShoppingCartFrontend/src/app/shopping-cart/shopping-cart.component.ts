import {Component, Inject, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {ShoppingCartService} from '../service/shopping-cart.service';
import {LoginService} from '../service/login.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  displayedColumns: string[] = ['name', 'subtotal', 'cant', 'delete'];

  cart: any;
  message: string;

  constructor(private shoppingCartService: ShoppingCartService, private loginService: LoginService, private router: Router, private dialog: MatDialog) { }

  openDialog() {
    this.dialog.open(DialogElement, {
      width: '50%',
      data: {message: this.message}
    });
  }

  ngOnInit(): void {

    if (this.loginService.authenticated){

        this.shoppingCartService.getCart(this.loginService.email).subscribe(cart =>{
          this.cart = cart;
          this.shoppingCartService.cart = cart;
          this.verifyProduct();
        }, error => {
          this.cart = null;
          this.shoppingCartService.cart = null;
          this.message = error.error.message;
          this.openDialog();
        });

    } else {
      this.router.navigate(['/login']);
    }
  }

  private verifyProduct(){
    let product = this.shoppingCartService.product;

    if (product) {

      let contains = false;

      if(this.cart.cartProducts){
        for(let element of this.cart.cartProducts){
          if(element.product.id === product.id){
            element.cant += product.cant;
            contains = true;
            break;
          }
        }
      }
      if (contains){
        this.update();
      } else {
        this.save(product);
      }
    }
  }

  deleteCartProduct(cartProduct){
    this.shoppingCartService.deleteCartProduct(cartProduct).subscribe(cart => {
      this.cart = cart;
      this.shoppingCartService.cart = cart;
    }, error => {
      this.cart = null;
      this.shoppingCartService.cart = null;
      this.message = error.error.message;
      this.openDialog();
    });
  }

  updateCant(cartProduct, event){
    if(event > 0){
      cartProduct.cant = event;
      this.update();
    }
  }

  deleteCart(){
    if(this.cart != null && this.cart.cartProducts != null && this.cart.cartProducts.length > 0){
      this.shoppingCartService.deleteCart().subscribe(cart => {
        this.cart = cart;
        this.shoppingCartService.cart = cart;
      }, error => {
        this.cart = null;
        this.shoppingCartService.cart = null;
        this.message = error.error.message;
        this.openDialog();
      });
    } else {
      this.message = 'No hay productos para eliminar';
      this.openDialog();
    }
  }

  update(){
    this.shoppingCartService.updateCart().subscribe(cart => {
      this.cart = cart;
      this.shoppingCartService.cart = cart;
      this.shoppingCartService.product = null;
    }, error => {
      this.cart = null;
      this.shoppingCartService.cart = null;
      this.shoppingCartService.product = null;
      this.message = error.error.message;
      this.openDialog();
    });
  }

  save(product){
    this.shoppingCartService.saveProductToCart(product, this.cart).subscribe( cart => {
      this.cart = cart;
      this.shoppingCartService.cart = cart;
      this.shoppingCartService.product = null;
    }, error => {
      this.cart = null;
      this.shoppingCartService.cart = null;
      this.shoppingCartService.product = null;
      this.message = error.error.message;
      this.openDialog();
    });
  }

  getCart(){
    this.shoppingCartService.getCart(this.loginService.email).subscribe(cart => {
      this.cart = cart;
      this.shoppingCartService.cart = cart;
    }, error => {
      this.cart = null;
      this.shoppingCartService.cart = null;
      this.message = error.error.message;
      this.openDialog();
    });
  }

  finishBuy(){

    if (this.cart != null && this.cart.cartProducts != null && this.cart.cartProducts.length > 0) {
      this.shoppingCartService.finishBuy().subscribe(boolean => {
        this.cart = null;
        this.shoppingCartService.cart = null;
        if (boolean) {
          this.message = 'Se guardó correctamente';
        } else {
          this.message = 'No se pudo guardar';
        }
        this.openDialog();
      }, error => {
        this.cart = null;
        this.shoppingCartService.cart = null;
        this.message = error.error.message;
        this.openDialog();
      });
    } else {
      this.message = 'No hay productos para comprar';
      this.openDialog();
    }
  }
}

@Component({
  selector: 'dialog-element',
  templateUrl: 'dialog-element.html',
})
export class DialogElement {

  constructor(
    public dialogRef: MatDialogRef<DialogElement>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  close(): void {
    this.dialogRef.close();
  }
}

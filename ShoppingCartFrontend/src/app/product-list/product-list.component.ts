import {Component, OnInit} from '@angular/core';
import {ProductsService} from '../service/products.service';
import {ShoppingCartService} from '../service/shopping-cart.service';
import {Router} from '@angular/router';

export interface Product {
  id: number,
  name: string,
  price: number
  cant: number
}

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  displayedColumns: string[] = ['name', 'price', 'cant', 'add'];
  products: Product[];

  constructor(private productsService: ProductsService, private shoppingCartService: ShoppingCartService, private router: Router) {
  }

  ngOnInit(): void {
    this.productsService.getAllProducts().subscribe(products => {
      this.products = products;
      for (let product of products) {
        console.log(product.name);
      }
    });
  }

  selectedProduct(product): void {
    this.shoppingCartService.product = product;
    this.router.navigate(['/shoppingCart']);
  }
}

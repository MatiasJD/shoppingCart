import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {ProductsService} from '../service/products.service';
import {MatPaginator} from '@angular/material/paginator';
import {ShoppingCartService} from '../service/shopping-cart.service';

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

  @Output() selected = new EventEmitter();

  displayedColumns: string[] = ['name', 'price', 'cant', 'add'];
  products: Product[];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(private productsService: ProductsService, private shoppingCartService: ShoppingCartService) {
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
  }
}

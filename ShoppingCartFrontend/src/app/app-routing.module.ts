import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PortalComponent} from './portal/portal.component';
import {ProductListComponent} from './product-list/product-list.component';
import {ShoppingCartComponent} from './shopping-cart/shopping-cart.component';
import {ClientReportsComponent} from './client-reports/client-reports.component';
import {LoginComponent} from './login/login.component';

const routes: Routes = [{path: 'portal',component: PortalComponent},
{path: 'products',component: ProductListComponent},
{path: 'shoppingCart',component: ShoppingCartComponent},
{path: 'clientReports',component: ClientReportsComponent},
{path: 'login',component: LoginComponent},
{path:'logout', redirectTo: '/portal', pathMatch: 'full'},
{path:'', redirectTo: '/portal', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

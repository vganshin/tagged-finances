import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';


import { AppComponent } from './app.component';
import { MainComponent } from './components/main/main.component';
import { CreateComponent } from './components/create/create.component';
import { DepositListComponent } from './components/deposit-list/deposit-list.component';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { FilterByDepositPipe } from './pipes/filter-by-deposit.pipe';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    CreateComponent,
    DepositListComponent,
    TransactionListComponent,
    FilterByDepositPipe
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

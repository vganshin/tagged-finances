import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Transaction} from '../model/transaction.model';
import {Deposit} from '../model/deposit.model';

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  GET_ALL_TRANSACTIONS = 'assets/transactions.json';
  GET_ALL_DEPOSITS = 'assets/deposits.json';

  constructor(private http: HttpClient) { }

  getTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(this.GET_ALL_TRANSACTIONS);
  }

  getDeposits(): Observable<Deposit[]> {
    return this.http.get<Deposit[]>(this.GET_ALL_DEPOSITS);
  }

}

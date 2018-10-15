import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Transaction} from '../model/transaction.model';
import {Deposit} from '../model/deposit.model';
import {formatDate} from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  GET_ALL_TRANSACTIONS = 'assets/transactions.json';
  GET_ALL_DEPOSITS = 'assets/deposits.json';
  POST_TRANSACTION = 'api/transactions/';
  POST_DEPOSIT = 'api/deposits/';

  constructor(private http: HttpClient) { }

  /*
  Expected response format:
  [{
    "id": 1,
    "date": "2018-10-10",
    "tags": "tag1,ag2",
    "deposit_id": 1,
    "amount": 200
  },...]
   */
  getTransactions(): Observable<Transaction[]> {
    return new Observable((observer) => {
      const result = [];

      this.getDeposits().subscribe((deposits: Deposit[]) => {
        this.http.get(this.GET_ALL_TRANSACTIONS).subscribe((transactions: any[]) => {
          transactions.forEach(transaction => {
            const localTransaction = new Transaction();
            localTransaction.id = transaction.id;
            localTransaction.tags = transaction.tags.split(',');
            localTransaction.deposit = deposits.find(deposit => deposit.id === transaction.deposit_id);
            localTransaction.date = new Date(transaction.date);
            localTransaction.amount = transaction.amount;
            result.push(localTransaction);
          });
        });
        console.log('get transactions');
        console.log(result);
        observer.next(result);
        observer.complete();
      });
    });


  }

  getDeposits(): Observable<Deposit[]> {
    return this.http.get<Deposit[]>(this.GET_ALL_DEPOSITS);
  }

  postDeposit(deposit: Deposit) {

    const body = Object.assign(deposit);
    delete body.id;

    console.log('post deposit');
    console.log(body);
    this.http.post(this.POST_DEPOSIT, body).subscribe();
  }

  /*
  Expected request format:
  [{
    "date": "2018-10-10",
    "tags": "tag1,ag2",
    "deposit_id": 1,
    "amount": 200
  },...]
   */
  postTransaction(transaction: Transaction) {
    const body = {
      'date': formatDate(transaction.date, 'yyyy-mm-dd', 'EN'),
      'tags': transaction.tags.join(','),
      'deposit_id': transaction.deposit.id,
      'amount': transaction.amount
    };

    console.log('post transaction');
    console.log(body);
    this.http.post(this.POST_TRANSACTION, body).subscribe();
  }

}

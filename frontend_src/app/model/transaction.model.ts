import {Deposit} from './deposit.model';

export class Transaction {

  id: number;
  date: Date;
  tags: string[];
  deposit: Deposit;
  amount: number;

  constructor() {

  }

}

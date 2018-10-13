import {Deposit} from './deposit.model';

export class Transaction {

  id: number;
  tags: string[];
  depositName: string;
  amount: number;

  constructor(tags: string[], depositName: string, amount: number) {
    this.tags = tags;
    this.depositName = depositName;
    this.amount = amount;
  }
}

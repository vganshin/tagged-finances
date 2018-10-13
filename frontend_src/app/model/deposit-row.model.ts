import {Deposit} from './deposit.model';

export class DepositRow {

  deposit: Deposit;
  edit: boolean;
  newBalance: number;

  constructor(deposit: Deposit) {
    this.deposit = deposit;
    this.newBalance = deposit.balance;
    this.edit = false;
  }

  getName(): string {
    return this.deposit.name;
  }

  getBalance(): number {
    return this.deposit.balance;
  }

  reset() {
    this.newBalance = this.deposit.balance;
    this.edit = false;
  }

}

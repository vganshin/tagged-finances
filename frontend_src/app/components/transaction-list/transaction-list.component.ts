import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';
import {Transaction} from '../../model/transaction.model';
import {Deposit} from '../../model/deposit.model';
import {BackendService} from '../../services/backend.service';

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {

  @Input() forDeposit: string;

  newTransactionForm: FormGroup;
  formVisible: boolean;

  deposits: Deposit[];
  transactions: Transaction[];

  constructor(private fb: FormBuilder,
              private backendService: BackendService) { }

  ngOnInit() {

    this.setupForm();

    this.backendService.getDeposits()
      .subscribe((deposits: Deposit[]) => this.deposits = deposits);
    this.backendService.getTransactions()
      .subscribe((transactions: Transaction[]) => this.transactions = transactions);

  }

  setupForm() {
    this.newTransactionForm = this.fb.group({
      deposit: ['', [
        Validators.required,
        Validators.pattern(/[\wА-я]/)
      ]],
      tags: ['', [
        Validators.required,
        Validators.pattern(/[\wА-я]/)
      ]],
      amount: ['', [
        Validators.required,
        Validators.pattern(/[0-9]+/)
      ]]
    });

    this.formVisible = false;
  }

  onSubmit() {

    const newTransaction = new Transaction();
    newTransaction.date = new Date();
    newTransaction.tags = this.newTransactionForm.controls.tags.value.split(',');
    newTransaction.deposit = this.newTransactionForm.controls.deposit.value;
    newTransaction.amount = this.newTransactionForm.controls.amount.value;


    this.backendService.postTransaction(newTransaction);
    this.transactions.push(newTransaction);

    this.hideForm();
  }

  showForm() {
    this.formVisible = true;
  }

  hideForm() {
    this.formVisible = false;
    this.newTransactionForm.controls.deposit.setValue('');
    this.newTransactionForm.controls.tags.setValue('');
    this.newTransactionForm.controls.amount.setValue(0);
  }

  isFormVisible(): boolean {
    return this.formVisible;
  }

}

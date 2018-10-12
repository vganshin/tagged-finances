import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {

  newTransactionForm: FormGroup;
  formVisible: boolean;

  @Input() forDeposit: string;

  deposits = [ 'bank', 'cash', 'bank 2'];

  transactions = [
    {
      'deposit': 'cash',
      'tags' : 'tag1, tag2',
      'amount' : 3000
    },
    {
      'deposit': 'bank',
      'tags': 'tag3',
      'amount' : 2000
    },
    {
      'deposit': 'bank',
      'tags': 'tag2',
      'amount' : 1000
    }
  ];

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
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
    this.transactions.push(
      {
        'deposit': this.newTransactionForm.controls.deposit.value,
        'tags': this.newTransactionForm.controls.tags.value,
        'amount': this.newTransactionForm.controls.amount.value
      });

    this.hideForm();
  }

  addItem() {
    this.formVisible = true;
  }

  hideForm() {
    this.formVisible = false;
    this.newTransactionForm.controls.deposit.setValue('');
    this.newTransactionForm.controls.tags.setValue('');
    this.newTransactionForm.controls.amount.setValue(0);

  }

}

import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';

@Component({
  selector: 'app-deposit-list',
  templateUrl: './deposit-list.component.html',
  styleUrls: ['./deposit-list.component.css']
})
export class DepositListComponent implements OnInit {

  newDepositForm: FormGroup;

  constructor(private fb: FormBuilder) { }

  newDepositFormVisible: boolean;

  deposits = [
    {
      'name': 'cash',
      'balance' : 200,
      'edit' : false
    },
    {
      'name': 'bank',
      'balance': 800,
      'edit' : false
    }
    ];

  ngOnInit() {
    this.newDepositForm = this.fb.group({
      name: ['', [
        Validators.required,
        Validators.pattern(/[\wА-я]/)
      ]
      ],
      balance: ['', [
        Validators.required,
        Validators.pattern(/[0-9]+/)
      ]
      ]
    });

    this.newDepositFormVisible = false;
  }

  onSubmit() {

    this.deposits.push(
      {
        'name': this.newDepositForm.controls.name.value,
        'balance': this.newDepositForm.controls.balance.value,
        'edit': false
      });

    this.hideForm();
  }

  addItem() {
    this.newDepositFormVisible = true;
  }

  hideForm() {
    this.newDepositFormVisible = false;
    this.newDepositForm.controls.name.setValue('');
    this.newDepositForm.controls.balance.setValue(0);
  }

}

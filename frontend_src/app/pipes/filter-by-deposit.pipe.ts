import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterByDeposit'
})
export class FilterByDepositPipe implements PipeTransform {

  transform(value: any[], args?: any): any {
    if (!value || !args) {
      return value;
    }

    return value.filter(item => item.deposit === args);
  }

}

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'accountFormatter' })
export class AccountFormatterPipe implements PipeTransform {
  transform(text: string): string {
  console.log('FORMATTING ...');
  const bank: string = text.slice(0 , 5);
  const agency: string = text.slice(5 , 10);
  const score: string = text.slice(10 , 16);
  const series: string = text.slice(16 , 19);
  const subAccount: string = text.slice(19, 21);
  const sprt  = '-';
  return bank + sprt + agency + sprt + score + sprt + series + sprt + subAccount;
  }
}

export interface OperationsApi {
  items: Operation[];
  totalCount: number;
}
export interface Operation {
  date: Date;
  id: string;
  amount: number;
  label: string;
  isCredential: boolean;
  account: string;
  type: OperationType;
}

export enum OperationType {
  DEPOSIT_CASH,
  DEPOSIT_CHECK,
  RETRIEVE_CASH,
  RETRIEVE_CHECK,
  TRANSFER,
  TAKING,
  CARD_PAYMENT,
  MONEY_TRANSFER,
  PHONE_PAYMENT,
}

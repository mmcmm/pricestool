export interface IVgoitem {
  id?: number;
  name?: string;
  op7day?: number;
  op30day?: number;
  sales?: number;
  qty?: number;
  minPrice?: number;
}

export const defaultValue: Readonly<IVgoitem> = {};

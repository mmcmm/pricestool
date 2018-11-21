export interface IVgoitem {
  id?: number;
  name?: string;
  category?: string;
  rarity?: string;
  type?: string;
  color?: string;
  image300px?: string;
  image600px?: string;
  suggestedPrice?: number;
  suggestedPrice7day?: number;
  suggestedPrice30day?: number;
}

export const defaultValue: Readonly<IVgoitem> = {};

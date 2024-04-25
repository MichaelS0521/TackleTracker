import dayjs from 'dayjs';

export interface IWeatherData {
  id?: number;
  condition?: string | null;
  temperature?: number | null;
  dateStamp?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IWeatherData> = {};

import { Moment } from 'moment';

export interface IChatPhoto {
  id?: number;
  creationDate?: Moment;
  imageContentType?: string;
  image?: any;
  chatUserId?: number;
}

export class ChatPhoto implements IChatPhoto {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public imageContentType?: string,
    public image?: any,
    public chatUserId?: number
  ) {}
}

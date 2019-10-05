import { Moment } from 'moment';
import { IChatNotification } from 'app/shared/model/chat-notification.model';

export interface IChatInvitation {
  id?: number;
  creationDate?: Moment;
  acceptance?: boolean;
  denial?: boolean;
  acceptanceDenialDate?: Moment;
  senderId?: number;
  receiverId?: number;
  chatRoomId?: number;
  chatNotifications?: IChatNotification[];
}

export class ChatInvitation implements IChatInvitation {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public acceptance?: boolean,
    public denial?: boolean,
    public acceptanceDenialDate?: Moment,
    public senderId?: number,
    public receiverId?: number,
    public chatRoomId?: number,
    public chatNotifications?: IChatNotification[]
  ) {
    this.acceptance = this.acceptance || false;
    this.denial = this.denial || false;
  }
}

import { Moment } from 'moment';
import { IChatMessage } from 'app/shared/model/chat-message.model';
import { IChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';
import { IChatNotification } from 'app/shared/model/chat-notification.model';
import { IChatInvitation } from 'app/shared/model/chat-invitation.model';

export interface IChatRoom {
  id?: number;
  creationDate?: Moment;
  roomName?: string;
  roomDescription?: string;
  privateRoom?: boolean;
  imageContentType?: string;
  image?: any;
  chatMessages?: IChatMessage[];
  chatRoomAllowedUsers?: IChatRoomAllowedUser[];
  chatNotifications?: IChatNotification[];
  chatInvitations?: IChatInvitation[];
  chatUserId?: number;
}

export class ChatRoom implements IChatRoom {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public roomName?: string,
    public roomDescription?: string,
    public privateRoom?: boolean,
    public imageContentType?: string,
    public image?: any,
    public chatMessages?: IChatMessage[],
    public chatRoomAllowedUsers?: IChatRoomAllowedUser[],
    public chatNotifications?: IChatNotification[],
    public chatInvitations?: IChatInvitation[],
    public chatUserId?: number
  ) {
    this.privateRoom = this.privateRoom || false;
  }
}

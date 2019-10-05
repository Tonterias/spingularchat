import { Moment } from 'moment';
import { IChatInvitation } from 'app/shared/model/chat-invitation.model';
import { ChatNotificationReason } from 'app/shared/model/enumerations/chat-notification-reason.model';

export interface IChatNotification {
  id?: number;
  creationDate?: Moment;
  chatNotificationReason?: ChatNotificationReason;
  chatUserId?: number;
  chatInvitations?: IChatInvitation[];
  chatRoomId?: number;
  chatMessageId?: number;
}

export class ChatNotification implements IChatNotification {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public chatNotificationReason?: ChatNotificationReason,
    public chatUserId?: number,
    public chatInvitations?: IChatInvitation[],
    public chatRoomId?: number,
    public chatMessageId?: number
  ) {}
}

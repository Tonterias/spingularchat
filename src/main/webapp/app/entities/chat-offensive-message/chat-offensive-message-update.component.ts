import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IChatOffensiveMessage, ChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';
import { ChatOffensiveMessageService } from './chat-offensive-message.service';
import { IChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from 'app/entities/chat-user/chat-user.service';
import { IChatMessage } from 'app/shared/model/chat-message.model';
import { ChatMessageService } from 'app/entities/chat-message/chat-message.service';

@Component({
  selector: 'jhi-chat-offensive-message-update',
  templateUrl: './chat-offensive-message-update.component.html'
})
export class ChatOffensiveMessageUpdateComponent implements OnInit {
  isSaving: boolean;

  chatusers: IChatUser[];

  chatmessages: IChatMessage[];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    isOffensive: [],
    chatUserId: [],
    chatMessageId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected chatOffensiveMessageService: ChatOffensiveMessageService,
    protected chatUserService: ChatUserService,
    protected chatMessageService: ChatMessageService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ chatOffensiveMessage }) => {
      this.updateForm(chatOffensiveMessage);
    });
    this.chatUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatUser[]>) => response.body)
      )
      .subscribe((res: IChatUser[]) => (this.chatusers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatMessageService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatMessage[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatMessage[]>) => response.body)
      )
      .subscribe((res: IChatMessage[]) => (this.chatmessages = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(chatOffensiveMessage: IChatOffensiveMessage) {
    this.editForm.patchValue({
      id: chatOffensiveMessage.id,
      creationDate: chatOffensiveMessage.creationDate != null ? chatOffensiveMessage.creationDate.format(DATE_TIME_FORMAT) : null,
      isOffensive: chatOffensiveMessage.isOffensive,
      chatUserId: chatOffensiveMessage.chatUserId,
      chatMessageId: chatOffensiveMessage.chatMessageId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const chatOffensiveMessage = this.createFromForm();
    if (chatOffensiveMessage.id !== undefined) {
      this.subscribeToSaveResponse(this.chatOffensiveMessageService.update(chatOffensiveMessage));
    } else {
      this.subscribeToSaveResponse(this.chatOffensiveMessageService.create(chatOffensiveMessage));
    }
  }

  private createFromForm(): IChatOffensiveMessage {
    return {
      ...new ChatOffensiveMessage(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      isOffensive: this.editForm.get(['isOffensive']).value,
      chatUserId: this.editForm.get(['chatUserId']).value,
      chatMessageId: this.editForm.get(['chatMessageId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatOffensiveMessage>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackChatUserById(index: number, item: IChatUser) {
    return item.id;
  }

  trackChatMessageById(index: number, item: IChatMessage) {
    return item.id;
  }
}
